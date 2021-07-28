package me.becycled.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.becycled.backend.exception.AlreadyExistException;
import me.becycled.backend.exception.AuthException;
import me.becycled.backend.exception.NotFoundException;
import me.becycled.backend.exception.WrongRequestException;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.community.Community;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.error.ErrorMessages;
import me.becycled.backend.model.utils.DomainUtils;
import me.becycled.backend.service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author I1yi4
 */
@RestController
@RequestMapping("/communities")
@Api(description = "Сообщества")
public class CommunityController {

    private final DaoFactory daoFactory;
    private final AccessService accessService;

    @Autowired
    public CommunityController(final DaoFactory daoFactory,
                               final AccessService accessService) {
        this.daoFactory = daoFactory;
        this.accessService = accessService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить сообщество по его идентификатору")
    public ResponseEntity<Community> getById(
        @ApiParam("Идентификатор сообщества") @PathVariable("id") final int id) {

        final Community community = daoFactory.getCommunityDao().getById(id);
        if (community == null) {
            throw new NotFoundException(ErrorMessages.notFound(Community.class));
        }
        return ResponseEntity.ok(community);
    }

    @RequestMapping(value = "/nickname/{nickname}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить сообщество по его никнейму")
    public ResponseEntity<Community> getByNickname(
        @ApiParam("Никнейм сообщества") @PathVariable("nickname") final String nickname) {

        final Community community = daoFactory.getCommunityDao().getByNickname(nickname);
        if (community == null) {
            throw new NotFoundException(ErrorMessages.notFound(Community.class));
        }
        return ResponseEntity.ok(community);
    }

    @RequestMapping(value = "/user/{login}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить список сообществ по логину пользователя, который является участником")
    public ResponseEntity<List<Community>> getCommunityWhichUserMemberByUserLogin(
        @ApiParam("Логин пользователя") @PathVariable("login") final String login) {

        final User user = daoFactory.getUserDao().getByLogin(login);
        if (user == null) {
            throw new NotFoundException(ErrorMessages.notFound(User.class));
        }
        return ResponseEntity.ok(daoFactory.getCommunityDao().getByMemberUserId(user.getId()));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить все сообщества")
    public ResponseEntity<List<Community>> getAll() {
        return ResponseEntity.ok(daoFactory.getCommunityDao().getAll());
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Создать сообщество")
    public ResponseEntity<Community> create(
        @ApiParam("Данные сообщества") @RequestBody final Community entity) {

        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        final Community community = daoFactory.getCommunityDao().getByNickname(entity.getNickname());
        if (community != null) {
            throw new AlreadyExistException(ErrorMessages.alreadyExist(Community.class));
        }

        entity.setOwnerUserId(curUser.getId());

        final List<Integer> userIds = new ArrayList<>(entity.getUserIds());
        userIds.add(curUser.getId());
        entity.setUserIds(userIds.stream().distinct().collect(Collectors.toList()));

        if (entity.getAvatar() == null) {
            entity.setAvatar(DomainUtils.DEFAULT_COMMUNITY_AVATAR_URL);
        }

        return ResponseEntity.ok(daoFactory.getCommunityDao().create(entity));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Обновить сообщество по его идентификатору")
    public ResponseEntity<Community> update(
        @ApiParam("Идентификатор сообщества") @PathVariable("id") final int id,
        @ApiParam("Данные сообщества") @RequestBody final Community entity) {

        if (id != entity.getId()) {
            throw new WrongRequestException(ErrorMessages.differentIdentifierInPathAndBody());
        }

        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        final Community community = daoFactory.getCommunityDao().getById(id);
        if (community == null) {
            throw new NotFoundException(ErrorMessages.notFound(Community.class));
        }

        if (!community.getOwnerUserId().equals(curUser.getId())) {
            throw new WrongRequestException(ErrorMessages.onlyOwnerCanUpdateEntity(Community.class));
        }

        return ResponseEntity.ok(daoFactory.getCommunityDao().update(entity));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Удалить сообщество по его идентификатору")
    public ResponseEntity<Integer> delete(
        @ApiParam("Идентификатор сообщества") @PathVariable("id") final int id) {

        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }
        final Community community = daoFactory.getCommunityDao().getById(id);
        if (community == null) {
            throw new NotFoundException(ErrorMessages.notFound(Community.class));
        }

        if (!community.getOwnerUserId().equals(curUser.getId())) {
            throw new WrongRequestException(ErrorMessages.onlyOwnerCanDeleteEntity(Community.class));
        }

        return ResponseEntity.ok(daoFactory.getCommunityDao().delete(id));
    }

    @RequestMapping(value = "/join/{id}", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Вступить в сообщество по его идентификатору")
    public ResponseEntity<Community> join(
        @ApiParam("Идентификатор сообщества") @PathVariable("id") final int id) {

        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        final Community community = daoFactory.getCommunityDao().getById(id);
        if (community == null) {
            throw new NotFoundException(ErrorMessages.notFound(Community.class));
        }

        if (community.getUserIds().contains(curUser.getId())) {
            throw new WrongRequestException(ErrorMessages.userAlreadyJoin());
        }

        final List<Integer> userIds = new ArrayList<>(community.getUserIds());
        userIds.add(curUser.getId());
        community.setUserIds(userIds);
        return ResponseEntity.ok(daoFactory.getCommunityDao().update(community));
    }

    @RequestMapping(value = "/leave/{id}", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Покинуть сообщество по его идентификатору")
    public ResponseEntity<Community> leave(
        @ApiParam("Идентификатор сообщества") @PathVariable("id") final int id) {

        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());

        }

        final Community community = daoFactory.getCommunityDao().getById(id);
        if (community == null) {
            throw new NotFoundException(ErrorMessages.notFound(Community.class));
        }

        if (!community.getUserIds().contains(curUser.getId())) {
            throw new WrongRequestException(ErrorMessages.userNotJoin());
        }

        final List<Integer> userIds = new ArrayList<>(community.getUserIds());
        userIds.remove(curUser.getId());
        community.setUserIds(userIds);
        return ResponseEntity.ok(daoFactory.getCommunityDao().update(community));
    }
}
