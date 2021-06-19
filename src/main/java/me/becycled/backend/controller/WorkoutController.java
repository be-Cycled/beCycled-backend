package me.becycled.backend.controller;

import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.entity.workout.Workout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author I1yi4
 */
@RestController
@RequestMapping("/workouts")
public class WorkoutController {

    private final DaoFactory daoFactory;

    @Autowired
    public WorkoutController(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable("id") final int id) {
        final Workout workout = daoFactory.getWorkoutDao().getById(id);
        if (workout == null) {
            return new ResponseEntity<>("Not found workout", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(workout);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Workout>> getAll() {
        return ResponseEntity.ok(daoFactory.getWorkoutDao().getAll());
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Workout> create(@RequestBody final Workout entity) {
        return ResponseEntity.ok(daoFactory.getWorkoutDao().create(entity));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable("id") final int id,
                                    @RequestBody final Workout entity) {
        if (id != entity.getId()) {
            return new ResponseEntity<>("Different identifiers in request path and body", HttpStatus.BAD_REQUEST);
        }

        final User curUser = daoFactory.getUserDao().getByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (curUser == null) {
            return new ResponseEntity<>("Auth error", HttpStatus.BAD_REQUEST);
        }

        final Workout workout = daoFactory.getWorkoutDao().getById(id);
        if (workout == null) {
            return new ResponseEntity<>("Workout not exist", HttpStatus.NOT_FOUND);
        }
        if (!workout.getOwnerUserId().equals(curUser.getId())) {
            return new ResponseEntity<>("Only owner can update route", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(daoFactory.getWorkoutDao().update(entity));
    }
}
