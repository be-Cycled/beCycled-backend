package me.becycled.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.telemetry.Tracker;
import me.becycled.backend.model.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author binakot
 */
@RestController
@RequestMapping("/trackers")
@Api(description = "Трекеры (навигационное оборудование)")
public class TrackerController {

    private final DaoFactory daoFactory;

    @Autowired
    public TrackerController(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить трекер по его идентификатору")
    public ResponseEntity<?> getById(
        @ApiParam("Идентификатор трекера") @PathVariable("id") final int id) {

        final Tracker tracker = daoFactory.getTrackerDao().getById(id);
        if (tracker == null) {
            return new ResponseEntity<>("Tracker is not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tracker);
    }

    @RequestMapping(value = "/imei/{imei}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить трекер по его IMEI")
    public ResponseEntity<?> getByImei(
        @ApiParam("IMEI трекера") @PathVariable("imei") final String imei) {

        final Tracker tracker = daoFactory.getTrackerDao().getByImei(imei.strip());
        if (tracker == null) {
            return new ResponseEntity<>("Tracker is not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tracker);
    }

    @RequestMapping(value = "/user/{login}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить трекер по идентификатору пользователя, к которому он привязан")
    public ResponseEntity<?> getByUserLogin(
        @ApiParam("Логин пользователя") @PathVariable("login") final String login) {

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
    @ApiOperation("Создать трекер")
    public ResponseEntity<Tracker> create(
        @ApiParam("Данные трекера") @RequestBody final Tracker entity) {

        return ResponseEntity.ok(daoFactory.getTrackerDao().create(entity));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить все трекеры")
    public ResponseEntity<List<Tracker>> getAll() {
        return ResponseEntity.ok(daoFactory.getTrackerDao().getAll());
    }
}
