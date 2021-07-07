package me.becycled.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.becycled.backend.exception.AuthException;
import me.becycled.backend.exception.NotFoundException;
import me.becycled.backend.exception.WrongRequestException;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.community.Community;
import me.becycled.backend.model.entity.competition.Competition;
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
@RequestMapping("/competitions")
@Api(description = "Соревнования")
public class CompetitionController {

    private final DaoFactory daoFactory;
    private final AccessService accessService;

    @Autowired
    public CompetitionController(final DaoFactory daoFactory,
                                 final AccessService accessService) {
        this.daoFactory = daoFactory;
        this.accessService = accessService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить соревнование по его идентификатору")
    public ResponseEntity<Competition> getById(
        @ApiParam("Идентификатор соревнования") @PathVariable("id") final int id) {

        final Competition competition = daoFactory.getCompetitionDao().getById(id);
        if (competition == null) {
            throw new NotFoundException(ErrorMessages.notFound(Competition.class));
        }
        return ResponseEntity.ok(competition);
    }

    @RequestMapping(value = "/user/{login}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить список соревнования по логину пользователя, который является участником")
    public ResponseEntity<List<Competition>> getCompetitionWhichUserMemberByUserLogin(
        @ApiParam("Логин пользователя") @PathVariable("login") final String login) {

        final User user = daoFactory.getUserDao().getByLogin(login);
        if (user == null) {
            throw new NotFoundException(ErrorMessages.notFound(User.class));
        }

        return ResponseEntity.ok(daoFactory.getCompetitionDao().getByMemberUserId(user.getId()));
    }

    @RequestMapping(value = "/community/{nickname}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить список соревнования по никнейму сообщества, где соревнования зарегистрированы")
    public ResponseEntity<List<Competition>> getByCommunityNickname(
        @ApiParam("Никнейм сообщества") @PathVariable("nickname") final String nickname) {

        final Community community = daoFactory.getCommunityDao().getByNickname(nickname);
        if (community == null) {
            throw new NotFoundException(ErrorMessages.notFound(Community.class));
        }
        return ResponseEntity.ok(daoFactory.getCompetitionDao().getByCommunityNickname(nickname));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить все соревнования")
    public ResponseEntity<List<Competition>> getAll() {
        return ResponseEntity.ok(daoFactory.getCompetitionDao().getAll());
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Создать соревнования")
    public ResponseEntity<Competition> create(
        @ApiParam("Данные соревнования") @RequestBody final Competition entity) {
        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        entity.setOwnerUserId(curUser.getId());

        final List<Integer> userIds = new ArrayList<>(entity.getUserIds());
        userIds.add(curUser.getId());
        entity.setUserIds(userIds.stream().distinct().collect(Collectors.toList()));

        return ResponseEntity.ok(daoFactory.getCompetitionDao().create(entity));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Обновить соревнование по его идентификатору")
    public ResponseEntity<Competition> update(
        @ApiParam("Идентификатор соревнования") @PathVariable("id") final int id,
        @ApiParam("Данные соревнования") @RequestBody final Competition entity) {

        if (id != entity.getId()) {
            throw new WrongRequestException(ErrorMessages.differentIdentifierInPathAndBody());
        }

        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        final Competition competition = daoFactory.getCompetitionDao().getById(id);
        if (competition == null) {
            throw new NotFoundException(ErrorMessages.notFound(Competition.class));
        }

        if (!competition.getOwnerUserId().equals(curUser.getId())) {
            throw new WrongRequestException(ErrorMessages.onlyOwnerCanUpdateEntity(Competition.class));
        }

        return ResponseEntity.ok(daoFactory.getCompetitionDao().update(entity));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Удалить соревнование по его идентификатору")
    public ResponseEntity<Integer> delete(
        @ApiParam("Идентификатор соревнования") @PathVariable("id") final int id) {
        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        final Competition competition = daoFactory.getCompetitionDao().getById(id);
        if (competition == null) {
            throw new NotFoundException(ErrorMessages.notFound(Competition.class));
        }

        if (!competition.getOwnerUserId().equals(curUser.getId())) {
            throw new WrongRequestException(ErrorMessages.onlyOwnerCanDeleteEntity(Competition.class));
        }

        return ResponseEntity.ok(daoFactory.getCompetitionDao().delete(id));
    }

    @RequestMapping(value = "/join/{id}", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Добавить на соревнование текущего пользователя по идентификатору соревнования")
    public ResponseEntity<Competition> join(
        @ApiParam("Идентификатор соревнования") @PathVariable("id") final int id) {
        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        final Competition competition = daoFactory.getCompetitionDao().getById(id);
        if (competition == null) {
            throw new NotFoundException(ErrorMessages.notFound(Competition.class));
        }

        if (competition.getUserIds().contains(curUser.getId())) {
            throw new WrongRequestException(ErrorMessages.userAlreadyJoin());
        }

        final List<Integer> userIds = new ArrayList<>(competition.getUserIds());
        userIds.add(curUser.getId());
        competition.setUserIds(userIds);
        return ResponseEntity.ok(daoFactory.getCompetitionDao().update(competition));
    }

    @RequestMapping(value = "/leave/{id}", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Удалить с соревнований текущего пользователя по идентификатору соревнования")
    public ResponseEntity<Competition> leave(
        @ApiParam("Идентификатор соревнования") @PathVariable("id") final int id) {
        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }
        final Competition competition = daoFactory.getCompetitionDao().getById(id);
        if (competition == null) {
            throw new NotFoundException(ErrorMessages.notFound(Competition.class));
        }

        if (!competition.getUserIds().contains(curUser.getId())) {
            throw new WrongRequestException(ErrorMessages.userNotJoin());
        }

        final List<Integer> userIds = new ArrayList<>(competition.getUserIds());
        userIds.remove(curUser.getId());
        competition.setUserIds(userIds);
        return ResponseEntity.ok(daoFactory.getCompetitionDao().update(competition));
    }
}

