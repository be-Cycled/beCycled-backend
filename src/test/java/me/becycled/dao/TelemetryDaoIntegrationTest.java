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
import java.util.List;

import static org.junit.Assert.assertEquals;

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
    void create() {
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
