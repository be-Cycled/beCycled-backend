package me.becycled.dao;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.telemetry.Telemetry;
import me.becycled.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author binakot
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TelemetryDaoIntegrationTest extends BaseIntegrationTest {

    @Test
    void createTelemetry() {
        final Telemetry testTelemetry = TestUtils.getTestTelemetry();
        daoFactory.getTelemetryDao().create(testTelemetry);

        final List<Telemetry> dbTelemetries = daoFactory.getTelemetryDao().getAll();
        assertEquals(1, dbTelemetries.size());

        final Telemetry dbTelemetry = dbTelemetries.get(0);
        assertEquals(testTelemetry.getTrackerId(), dbTelemetry.getTrackerId());
        assertEquals(testTelemetry.getFixTime(), dbTelemetry.getFixTime());
        assertEquals(testTelemetry.getLatitude(), dbTelemetry.getLatitude());
        assertEquals(testTelemetry.getLongitude(), dbTelemetry.getLongitude());
        assertEquals(testTelemetry.getAltitude(), dbTelemetry.getAltitude());
        assertEquals(testTelemetry.getSpeed(), dbTelemetry.getSpeed());
        assertEquals(testTelemetry.getCourse(), dbTelemetry.getCourse());
    }

    @Test
    void createTelemetries() {
        final Telemetry firstTelemetry = TestUtils.getTestTelemetry();
        daoFactory.getTelemetryDao().create(firstTelemetry);

        final Telemetry secondTelemetry = Telemetry.builder()
            .withTrackerId(1)
            .withFixTime(Instant.parse("2021-06-18T12:00:43Z"))
            .withServerTime(Instant.parse("2021-06-18T12:01:33Z"))
            .withLatitude(45.414414)
            .withLongitude(36.949160)
            .withAltitude(14.0)
            .withSpeed(65)
            .withCourse(189)
            .build();
        daoFactory.getTelemetryDao().create(secondTelemetry);

        final List<Telemetry> dbTelemetries = daoFactory.getTelemetryDao().getAll();
        assertEquals(2, dbTelemetries.size());

        final List<Telemetry> sortedTelemetries = dbTelemetries.stream()
            .sorted(Comparator.comparing(Telemetry::getServerTime))
            .collect(Collectors.toList());

        final Telemetry firstDbTelemetry = sortedTelemetries.get(0);
        assertEquals(firstTelemetry.getTrackerId(), firstDbTelemetry.getTrackerId());
        assertEquals(firstTelemetry.getFixTime(), firstDbTelemetry.getFixTime());
        assertEquals(firstTelemetry.getLatitude(), firstDbTelemetry.getLatitude());
        assertEquals(firstTelemetry.getLongitude(), firstDbTelemetry.getLongitude());
        assertEquals(firstTelemetry.getAltitude(), firstDbTelemetry.getAltitude());
        assertEquals(firstTelemetry.getSpeed(), firstDbTelemetry.getSpeed());

        final Telemetry secondDbTelemetry = sortedTelemetries.get(1);
        assertEquals(secondTelemetry.getTrackerId(), secondDbTelemetry.getTrackerId());
        assertEquals(secondTelemetry.getFixTime(), secondDbTelemetry.getFixTime());
        assertEquals(secondTelemetry.getLatitude(), secondDbTelemetry.getLatitude());
        assertEquals(secondTelemetry.getLongitude(), secondDbTelemetry.getLongitude());
        assertEquals(secondTelemetry.getAltitude(), secondDbTelemetry.getAltitude());
        assertEquals(secondTelemetry.getSpeed(), secondDbTelemetry.getSpeed());
        assertEquals(secondTelemetry.getCourse(), secondDbTelemetry.getCourse());
    }

    @Test
    void getLastByTrackerId() {
        final Telemetry firstTestTelemetry = TestUtils.getTestTelemetry();
        firstTestTelemetry.setFixTime(firstTestTelemetry.getFixTime().plusSeconds(10));
        daoFactory.getTelemetryDao().create(firstTestTelemetry);

        final Telemetry secondTestTelemetry = TestUtils.getTestTelemetry();
        secondTestTelemetry.setFixTime(secondTestTelemetry.getFixTime().plusSeconds(20));
        daoFactory.getTelemetryDao().create(secondTestTelemetry);

        final Telemetry thirdTestTelemetry = TestUtils.getTestTelemetry();
        thirdTestTelemetry.setFixTime(thirdTestTelemetry.getFixTime().plusSeconds(30));
        daoFactory.getTelemetryDao().create(thirdTestTelemetry);

        final Telemetry lastByTrackerId = daoFactory.getTelemetryDao().getLastByTrackerId(firstTestTelemetry.getTrackerId());
        assertEquals(Instant.parse("2021-06-18T12:00:00Z").plusSeconds(30), lastByTrackerId.getFixTime());
    }

    @Test
    void getRangeByTrackerId() {
        final Telemetry firstTestTelemetry = TestUtils.getTestTelemetry();
        firstTestTelemetry.setFixTime(firstTestTelemetry.getFixTime().plusSeconds(10));
        daoFactory.getTelemetryDao().create(firstTestTelemetry);

        final Telemetry secondTestTelemetry = TestUtils.getTestTelemetry();
        secondTestTelemetry.setFixTime(secondTestTelemetry.getFixTime().plusSeconds(20));
        daoFactory.getTelemetryDao().create(secondTestTelemetry);

        final Telemetry thirdTestTelemetry = TestUtils.getTestTelemetry();
        thirdTestTelemetry.setFixTime(thirdTestTelemetry.getFixTime().plusSeconds(30));
        daoFactory.getTelemetryDao().create(thirdTestTelemetry);

        final List<Telemetry> getRangeByTrackerId = daoFactory.getTelemetryDao().getRangeByTrackerId(
            firstTestTelemetry.getTrackerId(),
            Instant.parse("2021-06-18T12:00:00Z").plusSeconds(15),
            Instant.parse("2021-06-18T12:00:00Z").plusSeconds(25));
        assertEquals(1, getRangeByTrackerId.size());
        assertEquals(Instant.parse("2021-06-18T12:00:00Z").plusSeconds(20), getRangeByTrackerId.get(0).getFixTime());
    }

    @Test
    void getAll() {
        final Telemetry firstTestTelemetry = TestUtils.getTestTelemetry();
        firstTestTelemetry.setFixTime(firstTestTelemetry.getFixTime().plusSeconds(10));
        daoFactory.getTelemetryDao().create(firstTestTelemetry);

        final Telemetry secondTestTelemetry = TestUtils.getTestTelemetry();
        secondTestTelemetry.setFixTime(secondTestTelemetry.getFixTime().plusSeconds(20));
        daoFactory.getTelemetryDao().create(secondTestTelemetry);

        final Telemetry thirdTestTelemetry = TestUtils.getTestTelemetry();
        thirdTestTelemetry.setFixTime(thirdTestTelemetry.getFixTime().plusSeconds(30));
        daoFactory.getTelemetryDao().create(thirdTestTelemetry);

        final List<Telemetry> all = daoFactory.getTelemetryDao().getAll();
        assertEquals(3, all.size());
    }
}
