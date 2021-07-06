package me.becycled.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.community.Community;
import me.becycled.backend.model.entity.competition.Competition;
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
@RequestMapping("/competitions")
@Api(description = "Соревнования")
public class CompetitionController {

    private final DaoFactory daoFactory;

    @Autowired
    public CompetitionController(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить соревнование по его идентификатору")
    public ResponseEntity<?> getById(
        @ApiParam("Идентификатор соревнования") @PathVariable("id") final int id) {

        final Competition competition = daoFactory.getCompetitionDao().getById(id);
        if (competition == null) {
            return new ResponseEntity<>("Competition is not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(competition);
    }

    @RequestMapping(value = "/user/{login}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить список соревнования по логину пользователя, который является участником")
    public ResponseEntity<?> getByUserLogin(
        @ApiParam("Логин пользователя") @PathVariable("login") final String login) {

        final User user = daoFactory.getUserDao().getByLogin(login);
        if (user == null) {
            return new ResponseEntity<>("User is not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(daoFactory.getCompetitionDao().getAll().stream() // TODO getByCompetitorUserId
            .filter(c -> c.getUserIds().contains(user.getId()))
            .collect(Collectors.toList()));
    }

    @RequestMapping(value = "/community/{nickname}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить список соревнования по никнейму сообщества, где соревнования зарегистрированы")
    public ResponseEntity<?> getByCommunityNickname(
        @ApiParam("Никнейм сообщества") @PathVariable("nickname") final String nickname) {

        final Community community = daoFactory.getCommunityDao().getByNickname(nickname);
        if (community == null) {
            return new ResponseEntity<>("Community is not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(daoFactory.getCompetitionDao().getByCommunityNickname(nickname));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить все соревнования")
    public ResponseEntity<List<Competition>> getAll() {
        return ResponseEntity.ok(daoFactory.getCompetitionDao().getAll());
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Создать соревнование")
    public ResponseEntity<Competition> create(
        @ApiParam("Данные соревнования") @RequestBody final Competition entity) {

        return ResponseEntity.ok(daoFactory.getCompetitionDao().create(entity));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Обновить соревнование по его идентификатору")
    public ResponseEntity<?> update(
        @ApiParam("Идентификатор соревнования") @PathVariable("id") final int id,
        @ApiParam("Данные соревнования") @RequestBody final Competition entity) {

        if (id != entity.getId()) {
            return new ResponseEntity<>("Different identifiers in request path and body", HttpStatus.BAD_REQUEST);
        }

        final User curUser = daoFactory.getUserDao().getByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (curUser == null) {
            return new ResponseEntity<>("Auth error", HttpStatus.UNAUTHORIZED);
        }

        final Competition competition = daoFactory.getCompetitionDao().getById(id);
        if (competition == null) {
            return new ResponseEntity<>("Competition is not exist", HttpStatus.NOT_FOUND);
        }

        if (!competition.getOwnerUserId().equals(curUser.getId())) {
            return new ResponseEntity<>("Competition can be updated by owner only", HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(daoFactory.getCompetitionDao().update(entity));
    }

    @RequestMapping(value = "/join/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Добавить на соревнование текущего пользователя по идентификатору соревнования")
    public ResponseEntity<?> join(
        @ApiParam("Идентификатор соревнования") @PathVariable("id") final int id) {

        final Competition competition = daoFactory.getCompetitionDao().getById(id);
        if (competition == null) {
            return new ResponseEntity<>("Competition is not found", HttpStatus.NOT_FOUND);
        }

        final User curUser = daoFactory.getUserDao().getByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (curUser == null) {
            return new ResponseEntity<>("Auth error", HttpStatus.UNAUTHORIZED);
        }

        if (competition.getUserIds().contains(curUser.getId())) {
            return new ResponseEntity<>("Current user is already joined", HttpStatus.BAD_REQUEST);
        }

        final List<Integer> userIds = competition.getUserIds() != null ? new ArrayList<>(competition.getUserIds()) : new ArrayList<>();
        userIds.add(curUser.getId());
        competition.setUserIds(userIds);
        return ResponseEntity.ok(daoFactory.getCompetitionDao().update(competition));
    }

    @RequestMapping(value = "/leave/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Удалить с соревнований текущего пользователя по идентификатору соревнования")
    public ResponseEntity<?> leave(
        @ApiParam("Идентификатор соревнования") @PathVariable("id") final int id) {

        final Competition competition = daoFactory.getCompetitionDao().getById(id);
        if (competition == null) {
            return new ResponseEntity<>("Competition is not found", HttpStatus.NOT_FOUND);
        }

        final User curUser = daoFactory.getUserDao().getByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (curUser == null) {
            return new ResponseEntity<>("Auth error", HttpStatus.UNAUTHORIZED);
        }

        if (!competition.getUserIds().contains(curUser.getId())) {
            return new ResponseEntity<>("Current user is not joined", HttpStatus.BAD_REQUEST);
        }

        final List<Integer> userIds = competition.getUserIds() != null ? new ArrayList<>(competition.getUserIds()) : new ArrayList<>();
        userIds.remove(curUser.getId());
        competition.setUserIds(userIds);
        return ResponseEntity.ok(daoFactory.getCompetitionDao().update(competition));
    }
}
