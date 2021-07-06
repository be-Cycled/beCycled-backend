package me.becycled.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.community.Community;
import me.becycled.backend.model.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    public CommunityController(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить сообщество по его идентификатору")
    public ResponseEntity<?> getById(
        @ApiParam("Идентификатор сообщества") @PathVariable("id") final int id) {

        final Community community = daoFactory.getCommunityDao().getById(id);
        if (community == null) {
            return new ResponseEntity<>("Community is not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(community);
    }

    @RequestMapping(value = "/nickname/{nickname}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить сообщество по его никнейму")
    public ResponseEntity<?> getByNickname(
        @ApiParam("Никнейм сообщества") @PathVariable("nickname") final String nickname) {

        final Community community = daoFactory.getCommunityDao().getByNickname(nickname);
        if (community == null) {
            return new ResponseEntity<>("Community is not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(community);
    }

    @RequestMapping(value = "/nickname/{nickname}/users", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить список участников сообщества по его никнейму")
    public ResponseEntity<?> getUsersByNickname(
        @ApiParam("Никнейм сообщества") @PathVariable("nickname") final String nickname) {

        final Community community = daoFactory.getCommunityDao().getByNickname(nickname);
        if (community == null) {
            return new ResponseEntity<>("Community is not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(
            community.getUserIds().stream()
                .map(id -> daoFactory.getUserDao().getById(id))
                .collect(Collectors.toList()));
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

        return ResponseEntity.ok(daoFactory.getCommunityDao().create(entity));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Обновить сообщество по его идентификатору")
    public ResponseEntity<?> update(
        @ApiParam("Идентификатор сообщества") @PathVariable("id") final int id,
        @ApiParam("Данные сообщества") @RequestBody final Community entity) {

        if (id != entity.getId()) {
            return new ResponseEntity<>("Different identifiers in request path and body", HttpStatus.BAD_REQUEST);
        }

        final User curUser = daoFactory.getUserDao().getByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (curUser == null) {
            return new ResponseEntity<>("Auth error", HttpStatus.UNAUTHORIZED);
        }

        final Community community = daoFactory.getCommunityDao().getById(id);
        if (community == null) {
            return new ResponseEntity<>("Community is not found", HttpStatus.NOT_FOUND);
        }

        if (!community.getOwnerUserId().equals(curUser.getId())) {
            return new ResponseEntity<>("Community can be updated by owner only", HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(daoFactory.getCommunityDao().update(entity));
    }

    @RequestMapping(value = "/user/{login}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить список сообществ по логину пользователя, который является участником")
    public ResponseEntity<?> getByUserLogin(
        @ApiParam("Логин пользователя") @PathVariable("login") final String login) {

        final User user = daoFactory.getUserDao().getByLogin(login);
        if (user == null) {
            return new ResponseEntity<>("User is not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(daoFactory.getCommunityDao().getByMemberUserId(user.getId()));
    }

    @RequestMapping(value = "/join/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Вступить в сообщество по его идентификатору")
    public ResponseEntity<?> join(
        @ApiParam("Идентификатор сообщества") @PathVariable("id") final int id) {

        final Community community = daoFactory.getCommunityDao().getById(id);
        if (community == null) {
            return new ResponseEntity<>("Community is not found", HttpStatus.NOT_FOUND);
        }

        final User curUser = daoFactory.getUserDao().getByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (curUser == null) {
            return new ResponseEntity<>("Auth error", HttpStatus.UNAUTHORIZED);
        }

        if (community.getUserIds().contains(curUser.getId())) {
            return new ResponseEntity<>("Current user is already joined", HttpStatus.BAD_REQUEST);
        }

        final List<Integer> userIds = community.getUserIds() != null ? new ArrayList<>(community.getUserIds()) : new ArrayList<>();
        userIds.add(curUser.getId());
        community.setUserIds(userIds);
        return ResponseEntity.ok(daoFactory.getCommunityDao().update(community));
    }

    @RequestMapping(value = "/leave/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Покинуть сообщество по его идентификатору")
    public ResponseEntity<?> leave(
        @ApiParam("Идентификатор сообщества") @PathVariable("id") final int id) {

        final Community community = daoFactory.getCommunityDao().getById(id);
        if (community == null) {
            return new ResponseEntity<>("Community is not found", HttpStatus.NOT_FOUND);
        }

        final User curUser = daoFactory.getUserDao().getByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (curUser == null) {
            return new ResponseEntity<>("Auth error", HttpStatus.UNAUTHORIZED);
        }

        if (!community.getUserIds().contains(curUser.getId())) {
            return new ResponseEntity<>("Current user is not joined", HttpStatus.BAD_REQUEST);
        }

        final List<Integer> userIds = community.getUserIds() != null ? new ArrayList<>(community.getUserIds()) : new ArrayList<>();
        userIds.remove(curUser.getId());
        community.setUserIds(userIds);
        return ResponseEntity.ok(daoFactory.getCommunityDao().update(community));
    }
}
