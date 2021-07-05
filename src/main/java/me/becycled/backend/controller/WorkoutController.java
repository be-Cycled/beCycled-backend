package me.becycled.backend.controller;

import me.becycled.backend.exception.AuthException;
import me.becycled.backend.exception.NotFoundException;
import me.becycled.backend.exception.WrongRequestException;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.community.Community;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.entity.workout.Workout;
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
@RequestMapping("/workouts")
public class WorkoutController {

    private final DaoFactory daoFactory;
    private final AccessService accessService;

    @Autowired
    public WorkoutController(final DaoFactory daoFactory,
                             final AccessService accessService) {
        this.daoFactory = daoFactory;
        this.accessService = accessService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Workout> getById(@PathVariable("id") final int id) {
        final Workout workout = daoFactory.getWorkoutDao().getById(id);
        if (workout == null) {
            throw new NotFoundException(ErrorMessages.notFound(Workout.class));
        }
        return ResponseEntity.ok(workout);
    }

    @RequestMapping(value = "/user/{login}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Workout>> getWorkoutWhichUserMemberByUserLogin(@PathVariable("login") final String login) {
        final User user = daoFactory.getUserDao().getByLogin(login);
        if (user == null) {
            throw new NotFoundException(ErrorMessages.notFound(User.class));
        }

        return ResponseEntity.ok(daoFactory.getWorkoutDao().getByMemberUserId(user.getId()));
    }

    @RequestMapping(value = "/community/{nickname}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Workout>> getByCommunityNickname(@PathVariable("nickname") final String nickname) {
        final Community community = daoFactory.getCommunityDao().getByNickname(nickname);
        if (community == null) {
            throw new NotFoundException(ErrorMessages.notFound(Community.class));
        }
        return ResponseEntity.ok(daoFactory.getWorkoutDao().getByCommunityNickname(nickname));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Workout>> getAll() {
        return ResponseEntity.ok(daoFactory.getWorkoutDao().getAll());
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Workout> create(@RequestBody final Workout entity) {
        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        entity.setOwnerUserId(curUser.getId());

        final List<Integer> userIds = new ArrayList<>(entity.getUserIds());
        userIds.add(curUser.getId());
        entity.setUserIds(userIds.stream().distinct().collect(Collectors.toList()));

        return ResponseEntity.ok(daoFactory.getWorkoutDao().create(entity));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Workout> update(@PathVariable("id") final int id,
                                          @RequestBody final Workout entity) {
        if (id != entity.getId()) {
            throw new WrongRequestException(ErrorMessages.differentIdentifierInPathAndBody());
        }

        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        final Workout workout = daoFactory.getWorkoutDao().getById(id);
        if (workout == null) {
            throw new NotFoundException(ErrorMessages.notFound(Workout.class));
        }

        if (!workout.getOwnerUserId().equals(curUser.getId())) {
            throw new WrongRequestException(ErrorMessages.onlyOwnerCanUpdateEntity(Workout.class));
        }

        return ResponseEntity.ok(daoFactory.getWorkoutDao().update(entity));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> delete(@PathVariable("id") final int id) {

        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        final Workout workout = daoFactory.getWorkoutDao().getById(id);
        if (workout == null) {
            throw new NotFoundException(ErrorMessages.notFound(Workout.class));
        }

        if (!workout.getOwnerUserId().equals(curUser.getId())) {
            throw new WrongRequestException(ErrorMessages.onlyOwnerCanDeleteEntity(Workout.class));
        }

        return ResponseEntity.ok(daoFactory.getWorkoutDao().delete(id));
    }

    @RequestMapping(value = "/join/{id}", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Workout> join(@PathVariable("id") final int id) {
        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        final Workout workout = daoFactory.getWorkoutDao().getById(id);
        if (workout == null) {
            throw new NotFoundException(ErrorMessages.notFound(Workout.class));
        }

        if (workout.getUserIds().contains(curUser.getId())) {
            throw new WrongRequestException(ErrorMessages.userAlreadyJoin());
        }

        final List<Integer> userIds = new ArrayList<>(workout.getUserIds());
        userIds.add(curUser.getId());
        workout.setUserIds(userIds);
        return ResponseEntity.ok(daoFactory.getWorkoutDao().update(workout));
    }

    @RequestMapping(value = "/leave/{id}", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Workout> leave(@PathVariable("id") final int id) {
        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        final Workout workout = daoFactory.getWorkoutDao().getById(id);
        if (workout == null) {
            throw new NotFoundException(ErrorMessages.notFound(Workout.class));
        }

        if (!workout.getUserIds().contains(curUser.getId())) {
            throw new WrongRequestException(ErrorMessages.userNotJoin());
        }

        final List<Integer> userIds = new ArrayList<>(workout.getUserIds());
        userIds.remove(curUser.getId());
        workout.setUserIds(userIds);
        return ResponseEntity.ok(daoFactory.getWorkoutDao().update(workout));
    }
}
