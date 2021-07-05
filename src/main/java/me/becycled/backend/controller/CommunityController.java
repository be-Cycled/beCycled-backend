package me.becycled.backend.controller;

import me.becycled.backend.exception.AlreadyExistException;
import me.becycled.backend.exception.AuthException;
import me.becycled.backend.exception.NotFoundException;
import me.becycled.backend.exception.WrongRequestException;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.community.Community;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.error.ErrorMessages;
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
    public ResponseEntity<Community> getById(@PathVariable("id") final int id) {
        final Community community = daoFactory.getCommunityDao().getById(id);
        if (community == null) {
            throw new NotFoundException(ErrorMessages.notFound(Community.class));
        }
        return ResponseEntity.ok(community);
    }

    @RequestMapping(value = "/nickname/{nickname}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Community> getByNickname(@PathVariable("nickname") final String nickname) {
        final Community community = daoFactory.getCommunityDao().getByNickname(nickname);
        if (community == null) {
            throw new NotFoundException(ErrorMessages.notFound(Community.class));
        }
        return ResponseEntity.ok(community);
    }

    @RequestMapping(value = "/nickname/{nickname}/users", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUsersByCommunityNickname(@PathVariable("nickname") final String nickname) {
        final Community community = daoFactory.getCommunityDao().getByNickname(nickname);
        if (community == null) {
            throw new NotFoundException(ErrorMessages.notFound(Community.class));
        }

        return ResponseEntity.ok(daoFactory.getUserDao().getByIds(community.getUserIds()));
    }

    @RequestMapping(value = "/user/{login}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Community>> getCommunityWhichUserMemberByUserLogin(@PathVariable("login") final String login) {
        final User user = daoFactory.getUserDao().getByLogin(login);
        if (user == null) {
            throw new NotFoundException(ErrorMessages.notFound(User.class));
        }
        return ResponseEntity.ok(daoFactory.getCommunityDao().getByMemberUserId(user.getId()));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Community>> getAll() {
        return ResponseEntity.ok(daoFactory.getCommunityDao().getAll());
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Community> create(@RequestBody final Community entity) {

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

        return ResponseEntity.ok(daoFactory.getCommunityDao().create(entity));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Community> update(@PathVariable("id") final int id,
                                            @RequestBody final Community entity) {
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
    public ResponseEntity<Integer> delete(@PathVariable("id") final int id) {

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
    public ResponseEntity<Community> join(@PathVariable("id") final int id) {
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
    public ResponseEntity<Community> leave(@PathVariable("id") final int id) {
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
