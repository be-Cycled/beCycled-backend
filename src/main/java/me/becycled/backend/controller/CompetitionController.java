package me.becycled.backend.controller;

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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author I1yi4
 */
@RestController
@RequestMapping("/competitions")
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
    public ResponseEntity<Competition> getById(@PathVariable("id") final int id) {
        final Competition competition = daoFactory.getCompetitionDao().getById(id);
        if (competition == null) {
            throw new NotFoundException(ErrorMessages.notFound(Competition.class));
        }
        return ResponseEntity.ok(competition);
    }

    @RequestMapping(value = "/user/{login}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Competition>> getCompetitionWhichUserMemberByUserLogin(@PathVariable("login") final String login) {
        final User user = daoFactory.getUserDao().getByLogin(login);
        if (user == null) {
            throw new NotFoundException(ErrorMessages.notFound(User.class));
        }

        return ResponseEntity.ok(daoFactory.getCompetitionDao().getByMemberUserId(user.getId()));
    }

    @RequestMapping(value = "/community/{nickname}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Competition>> getByCommunityNickname(@PathVariable("nickname") final String nickname) {
        final Community community = daoFactory.getCommunityDao().getByNickname(nickname);
        if (community == null) {
            throw new NotFoundException(ErrorMessages.notFound(Community.class));
        }
        return ResponseEntity.ok(daoFactory.getCompetitionDao().getByCommunityNickname(nickname));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Competition>> getAll() {
        return ResponseEntity.ok(daoFactory.getCompetitionDao().getAll());
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Competition> create(@RequestBody final Competition entity) {
        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        entity.setOwnerUserId(curUser.getId());

        return ResponseEntity.ok(daoFactory.getCompetitionDao().create(entity));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Competition> update(@PathVariable("id") final int id,
                                              @RequestBody final Competition entity) {
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
    public ResponseEntity<Integer> delete(@PathVariable("id") final int id) {

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

    @RequestMapping(value = "/join/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> join(@PathVariable("id") final int id) {
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
    public ResponseEntity<?> leave(@PathVariable("id") final int id) {
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
