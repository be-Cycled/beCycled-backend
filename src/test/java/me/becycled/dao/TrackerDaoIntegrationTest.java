package me.becycled.dao;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.telemetry.Tracker;
import me.becycled.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
class TrackerDaoIntegrationTest extends BaseIntegrationTest {

    @Test
    void create() {
        final Tracker testTracker = TestUtils.getTestTracker();
        daoFactory.getTrackerDao().create(testTracker);

        final List<Tracker> dbTrackers = daoFactory.getTrackerDao().getAll();
        assertEquals(1, dbTrackers.size());

        final Tracker dbTracker = dbTrackers.get(0);
        assertEquals(testTracker.getId(), dbTracker.getId());
        assertEquals(testTracker.getUserId(), dbTracker.getUserId());
        assertEquals(testTracker.getImei(), dbTracker.getImei());
    }

    @Test
    void getById() {
        final Tracker testTracker = TestUtils.getTestTracker();
        daoFactory.getTrackerDao().create(testTracker);

        assertEquals(testTracker, daoFactory.getTrackerDao().getById(testTracker.getId()));
    }

    @Test
    void getByImei() {
        final Tracker testTracker = TestUtils.getTestTracker();
        daoFactory.getTrackerDao().create(testTracker);

        assertEquals(testTracker, daoFactory.getTrackerDao().getByImei(testTracker.getImei()));
    }

    @Test
    void getAll() {
        final Tracker firstTestTracker = TestUtils.getTestTracker();
        firstTestTracker.setImei("100000000000000");
        daoFactory.getTrackerDao().create(firstTestTracker);

        final Tracker secondTestTracker = TestUtils.getTestTracker();
        secondTestTracker.setImei("200000000000000");
        daoFactory.getTrackerDao().create(secondTestTracker);

        final Tracker thirdTestTracker = TestUtils.getTestTracker();
        thirdTestTracker.setImei("300000000000000");
        daoFactory.getTrackerDao().create(thirdTestTracker);

        final List<Tracker> all = daoFactory.getTrackerDao().getAll();
        assertEquals(3, all.size());
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}
