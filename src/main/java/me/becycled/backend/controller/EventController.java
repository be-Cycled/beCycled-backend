package me.becycled.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.becycled.backend.exception.AuthException;
import me.becycled.backend.exception.NotFoundException;
import me.becycled.backend.exception.WrongRequestException;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.community.Community;
import me.becycled.backend.model.entity.event.Event;
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
@RequestMapping("/events")
@Api(description = "События")
public class EventController {

    private final DaoFactory daoFactory;
    private final AccessService accessService;

    @Autowired
    public EventController(final DaoFactory daoFactory,
                           final AccessService accessService) {
        this.daoFactory = daoFactory;
        this.accessService = accessService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить событие по ее идентификатору")
    public ResponseEntity<Event> getById(
        @ApiParam("Идентификатор события") @PathVariable("id") final int id) {

        final Event event = daoFactory.getEventDao().getById(id);
        if (event == null) {
            throw new NotFoundException(ErrorMessages.notFound(Event.class));
        }
        return ResponseEntity.ok(event);
    }

    @RequestMapping(value = "/user/{login}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить список событий по логину пользователя, который является участником")
    public ResponseEntity<List<Event>> getByMemberUserLogin(
        @ApiParam("Логин пользователя") @PathVariable("login") final String login) {

        final User user = daoFactory.getUserDao().getByLogin(login);
        if (user == null) {
            throw new NotFoundException(ErrorMessages.notFound(User.class));
        }

        return ResponseEntity.ok(daoFactory.getEventDao().getByMemberUserId(user.getId()));
    }

    @RequestMapping(value = "/community/{nickname}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить список событий по никнейму сообщества, в котором они созданы")
    public ResponseEntity<List<Event>> getByCommunityNickname(
        @ApiParam("Никнейм сообщества") @PathVariable("nickname") final String nickname) {

        final Community community = daoFactory.getCommunityDao().getByNickname(nickname);
        if (community == null) {
            throw new NotFoundException(ErrorMessages.notFound(Community.class));
        }
        return ResponseEntity.ok(daoFactory.getEventDao().getByCommunityNickname(nickname));
    }

    @RequestMapping(value = "/affiche", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить список текущих и предстоящих событий")
    public ResponseEntity<List<Event>> getAffiche() {
        return ResponseEntity.ok(daoFactory.getEventDao().getAffiche());
    }

    @RequestMapping(value = "/feed", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить список прошедших событий ")
    public ResponseEntity<List<Event>> getFeed() {
        return ResponseEntity.ok(daoFactory.getEventDao().getFeed());
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить все события")
    public ResponseEntity<List<Event>> getAll() {
        return ResponseEntity.ok(daoFactory.getEventDao().getAll());
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Создать событие")
    public ResponseEntity<Event> create(
        @ApiParam("Данные события") @RequestBody final Event entity) {

        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        entity.setOwnerUserId(curUser.getId());

        final List<Integer> userIds = new ArrayList<>(entity.getMemberUserIds());
        userIds.add(curUser.getId());
        entity.setMemberUserIds(userIds.stream().distinct().collect(Collectors.toList()));

        return ResponseEntity.ok(daoFactory.getEventDao().create(entity));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Обновить событие по ее идентификатору")
    public ResponseEntity<Event> update(
        @ApiParam("Идентификатор события") @PathVariable("id") final int id,
        @ApiParam("Данные события") @RequestBody final Event entity) {

        if (id != entity.getId()) {
            throw new WrongRequestException(ErrorMessages.differentIdentifierInPathAndBody());
        }

        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        final Event event = daoFactory.getEventDao().getById(id);
        if (event == null) {
            throw new NotFoundException(ErrorMessages.notFound(Event.class));
        }

        if (!event.getOwnerUserId().equals(curUser.getId())) {
            throw new WrongRequestException(ErrorMessages.onlyOwnerCanUpdateEntity(Event.class));
        }

        return ResponseEntity.ok(daoFactory.getEventDao().update(entity));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Удалить событие по ее идентификатору")
    public ResponseEntity<Integer> delete(
        @PathVariable("id") final int id) {

        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        final Event event = daoFactory.getEventDao().getById(id);
        if (event == null) {
            throw new NotFoundException(ErrorMessages.notFound(Event.class));
        }

        if (!event.getOwnerUserId().equals(curUser.getId())) {
            throw new WrongRequestException(ErrorMessages.onlyOwnerCanDeleteEntity(Event.class));
        }

        return ResponseEntity.ok(daoFactory.getEventDao().delete(id));
    }

    @RequestMapping(value = "/join/{id}", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Добавить на событие текущего пользователя по идентификатору события")
    public ResponseEntity<Event> join(
        @ApiParam("Идентификатор события") @PathVariable("id") final int id) {

        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        final Event event = daoFactory.getEventDao().getById(id);
        if (event == null) {
            throw new NotFoundException(ErrorMessages.notFound(Event.class));
        }

        if (event.getMemberUserIds().contains(curUser.getId())) {
            throw new WrongRequestException(ErrorMessages.userAlreadyJoin());
        }

        final List<Integer> userIds = new ArrayList<>(event.getMemberUserIds());
        userIds.add(curUser.getId());
        event.setMemberUserIds(userIds);

        return ResponseEntity.ok(daoFactory.getEventDao().update(event));
    }

    @RequestMapping(value = "/leave/{id}", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Удалить из события текущего пользователя по идентификатору события")
    public ResponseEntity<Event> leave(
        @ApiParam("Идентификатор события") @PathVariable("id") final int id) {

        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        final Event event = daoFactory.getEventDao().getById(id);
        if (event == null) {
            throw new NotFoundException(ErrorMessages.notFound(Event.class));
        }

        if (!event.getMemberUserIds().contains(curUser.getId())) {
            throw new WrongRequestException(ErrorMessages.userNotJoin());
        }

        final List<Integer> userIds = new ArrayList<>(event.getMemberUserIds());
        userIds.remove(curUser.getId());
        event.setMemberUserIds(userIds);

        return ResponseEntity.ok(daoFactory.getEventDao().update(event));
    }
}
