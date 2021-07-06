package me.becycled.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.community.Community;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.entity.workout.Workout;
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
@RequestMapping("/workouts")
@Api(description = "Тренировки")
public class WorkoutController {

    private final DaoFactory daoFactory;

    @Autowired
    public WorkoutController(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить тренировку по ее идентификатору")
    public ResponseEntity<?> getById(@PathVariable("id") final int id) {
        final Workout workout = daoFactory.getWorkoutDao().getById(id);
        if (workout == null) {
            return new ResponseEntity<>("Workout is not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(workout);
    }

    @RequestMapping(value = "/user/{login}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить список тренировок по логину пользователя, который в них учавствует")
    public ResponseEntity<?> getByUserLogin(@PathVariable("login") final String login) {
        final User user = daoFactory.getUserDao().getByLogin(login);
        if (user == null) {
            return new ResponseEntity<>("User is not found", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(daoFactory.getWorkoutDao().getAll().stream() // // TODO getByMemberUserId
            .filter(w -> w.getUserIds().contains(user.getId()))
            .collect(Collectors.toList()));
    }

    @RequestMapping(value = "/community/{nickname}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить список тренировок по никнейму сообщества, в котором они зарегестрированы")
    public ResponseEntity<?> getByCommunityNickname(@PathVariable("nickname") final String nickname) {
        final Community community = daoFactory.getCommunityDao().getByNickname(nickname);
        if (community == null) {
            return new ResponseEntity<>("Community is not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(daoFactory.getWorkoutDao().getByCommunityNickname(nickname));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить все тренировки")
    public ResponseEntity<List<Workout>> getAll() {
        return ResponseEntity.ok(daoFactory.getWorkoutDao().getAll());
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Создать тренировку")
    public ResponseEntity<Workout> create(@RequestBody final Workout entity) {
        return ResponseEntity.ok(daoFactory.getWorkoutDao().create(entity));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Обновить тренировку по ее идентификатору")
    public ResponseEntity<?> update(@PathVariable("id") final int id,
                                    @RequestBody final Workout entity) {
        if (id != entity.getId()) {
            return new ResponseEntity<>("Different identifiers in request path and body", HttpStatus.BAD_REQUEST);
        }

        final User curUser = daoFactory.getUserDao().getByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (curUser == null) {
            return new ResponseEntity<>("Auth error", HttpStatus.UNAUTHORIZED);
        }

        final Workout workout = daoFactory.getWorkoutDao().getById(id);
        if (workout == null) {
            return new ResponseEntity<>("Workout is not exist", HttpStatus.NOT_FOUND);
        }

        if (!workout.getOwnerUserId().equals(curUser.getId())) {
            return new ResponseEntity<>("Workout can be updated by owner only", HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(daoFactory.getWorkoutDao().update(entity));
    }

    @RequestMapping(value = "/join/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Добавить на тренировку текущего пользователя по идентификатору тренировки")
    public ResponseEntity<?> join(@PathVariable("id") final int id) {
        final Workout workout = daoFactory.getWorkoutDao().getById(id);
        if (workout == null) {
            return new ResponseEntity<>("Workout is not found", HttpStatus.NOT_FOUND);
        }

        final User curUser = daoFactory.getUserDao().getByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (curUser == null) {
            return new ResponseEntity<>("Auth error", HttpStatus.UNAUTHORIZED);
        }

        if (workout.getUserIds().contains(curUser.getId())) {
            return new ResponseEntity<>("Current user is already joined", HttpStatus.BAD_REQUEST);
        }

        final List<Integer> userIds = workout.getUserIds() != null ? new ArrayList<>(workout.getUserIds()) : new ArrayList<>();
        userIds.add(curUser.getId());
        workout.setUserIds(userIds);
        return ResponseEntity.ok(daoFactory.getWorkoutDao().update(workout));
    }

    @RequestMapping(value = "/leave/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Удалить с тренировки текущего пользователя по идентификатору тренировки")
    public ResponseEntity<?> leave(@PathVariable("id") final int id) {
        final Workout workout = daoFactory.getWorkoutDao().getById(id);
        if (workout == null) {
            return new ResponseEntity<>("Workout is not found", HttpStatus.NOT_FOUND);
        }

        final User curUser = daoFactory.getUserDao().getByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (curUser == null) {
            return new ResponseEntity<>("Auth error", HttpStatus.UNAUTHORIZED);
        }

        if (!workout.getUserIds().contains(curUser.getId())) {
            return new ResponseEntity<>("Current user is not joined", HttpStatus.BAD_REQUEST);
        }

        final List<Integer> userIds = workout.getUserIds() != null ? new ArrayList<>(workout.getUserIds()) : new ArrayList<>();
        userIds.remove(curUser.getId());
        workout.setUserIds(userIds);
        return ResponseEntity.ok(daoFactory.getWorkoutDao().update(workout));
    }
}
