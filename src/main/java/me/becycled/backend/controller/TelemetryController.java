package me.becycled.backend.controller;

import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.telemetry.Telemetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author binakot
 */
@RestController
@RequestMapping("/telemetries")
public class TelemetryController {

    private final DaoFactory daoFactory;

    @Autowired
    public TelemetryController(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @RequestMapping(value = "/last/{trackerId}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLastByTrackerId(@PathVariable("trackerId") final int trackerId) {
        final Telemetry telemetry = daoFactory.getTelemetryDao().getLastByTrackerId(trackerId);
        if (telemetry == null) {
            return new ResponseEntity<>("Telemetry is not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(telemetry);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRangeByTrackerId(@RequestParam("trackerId") final int trackerId,
                                                 @RequestParam("from") final Instant from,
                                                 @RequestParam("to") final Instant to) {
        return ResponseEntity.ok(daoFactory.getTelemetryDao().getRangeByTrackerId(trackerId, from, to));
    }
}
