package me.becycled.backend.controller;

import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.telemetry.Tracker;
import me.becycled.backend.model.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author binakot
 */
@RestController
@RequestMapping("/trackers")
public class TrackerController {

    private final DaoFactory daoFactory;

    @Autowired
    public TrackerController(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable("id") final int id) {
        final Tracker tracker = daoFactory.getTrackerDao().getById(id);
        if (tracker == null) {
            return new ResponseEntity<>("Tracker is not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tracker);
    }

    @RequestMapping(value = "/imei/{imei}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByImei(@PathVariable("imei") final String imei) {
        final Tracker tracker = daoFactory.getTrackerDao().getByImei(imei.strip());
        if (tracker == null) {
            return new ResponseEntity<>("Tracker is not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tracker);
    }

    @RequestMapping(value = "/user/{login}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByUserLogin(@PathVariable("login") final String login) {
        final User user = daoFactory.getUserDao().getByLogin(login);
        if (user == null) {
            return new ResponseEntity<>("User is not found", HttpStatus.NOT_FOUND);
        }

        final Tracker tracker = daoFactory.getTrackerDao().getAll().stream()
            .filter(t -> t.getUserId().equals(user.getId())).findFirst()
            .orElse(null);
        if (tracker == null) {
            return new ResponseEntity<>("Tracker is not found", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(tracker);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Tracker> create(@RequestBody final Tracker entity) {
        return ResponseEntity.ok(daoFactory.getTrackerDao().create(entity));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Tracker>> getAll() {
        return ResponseEntity.ok(daoFactory.getTrackerDao().getAll());
    }
}
