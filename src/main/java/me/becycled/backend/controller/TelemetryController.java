package me.becycled.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(description = "Телеметрии трекеров")
public class TelemetryController {

    private final DaoFactory daoFactory;

    @Autowired
    public TelemetryController(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @RequestMapping(value = "/last/{trackerId}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить последнюю телеметрию по идентификатору трекера")
    public ResponseEntity<?> getLastByTrackerId(
        @ApiParam("Идентификатор трекера") @PathVariable("trackerId") final int trackerId) {

        final Telemetry telemetry = daoFactory.getTelemetryDao().getLastByTrackerId(trackerId);
        if (telemetry == null) {
            return new ResponseEntity<>("Telemetry is not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(telemetry);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить список телеметри по идентификатору трекера за указанный временной интервал")
    public ResponseEntity<?> getRangeByTrackerId(
        @ApiParam("Идентификатор трекера") @RequestParam("trackerId") final int trackerId,
        @ApiParam("Время начала в ISO8601") @RequestParam("from") final Instant from,
        @ApiParam("Время окончания в ISO8601") @RequestParam("to") final Instant to) {

        return ResponseEntity.ok(daoFactory.getTelemetryDao().getRangeByTrackerId(trackerId, from, to));
    }
}
