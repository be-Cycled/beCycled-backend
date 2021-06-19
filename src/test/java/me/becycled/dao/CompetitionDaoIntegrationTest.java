package me.becycled.dao;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.route.Route;
import me.becycled.backend.model.entity.route.SportType;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.entity.workout.Workout;
import me.becycled.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author I1yi4
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompetitionDaoIntegrationTest extends BaseIntegrationTest {

    @Test
    void create() {
        final User testUser = TestUtils.getTestUser();
        daoFactory.getUserDao().create(testUser);

        final Route testRoute = TestUtils.getTestRoute();
        daoFactory.getRouteDao().create(testRoute);

        final Workout workout = new Workout();
        workout.setOwnerUserId(1);
        workout.setIsPrivate(false);
        workout.setRouteId(1);
        workout.setSportTypes(List.of(SportType.BICYCLE));
        workout.setStartDate(Instant.parse("2021-06-19T00:00:00Z"));
        workout.setDescription("description");
        final Workout bdWorkout = daoFactory.getWorkoutDao().create(workout);
        assertNotNull(bdWorkout.getId());
        assertNotNull(bdWorkout.getCreatedAt());

        workout.setId(bdWorkout.getId());
        workout.setCreatedAt(bdWorkout.getCreatedAt());

        assertEquals(workout.getId(), bdWorkout.getId());
        assertEquals(workout.getOwnerUserId(), bdWorkout.getOwnerUserId());
        assertEquals(workout.getRouteId(), bdWorkout.getRouteId());
        assertEquals(workout.getSportTypes(), bdWorkout.getSportTypes());
        assertEquals(workout.getStartDate(), bdWorkout.getStartDate());
        assertEquals(workout.getCommunityId(), bdWorkout.getCommunityId());
        assertEquals(workout.getDescription(), bdWorkout.getDescription());
        assertEquals(workout.getCreatedAt(), bdWorkout.getCreatedAt());
    }

    @Test
    void getById() {
        final User testUser = TestUtils.getTestUser();
        daoFactory.getUserDao().create(testUser);

        final Route testRoute = TestUtils.getTestRoute();
        daoFactory.getRouteDao().create(testRoute);

        final Workout workout = daoFactory.getWorkoutDao().create(TestUtils.getTestWorkout());
        final Workout createdWorkout = daoFactory.getWorkoutDao().getById(workout.getId());

        assertEquals(createdWorkout, workout);
    }

    @Test
    void update() {
        final User testUser = TestUtils.getTestUser();
        daoFactory.getUserDao().create(testUser);
        final Route testRoute = TestUtils.getTestRoute();
        daoFactory.getRouteDao().create(testRoute);

        final Workout community = daoFactory.getWorkoutDao().create(TestUtils.getTestWorkout());

        final Workout testWorkout = new Workout();
        testWorkout.setId(community.getId());
        testWorkout.setOwnerUserId(1);
        testWorkout.setIsPrivate(false);
        testWorkout.setRouteId(1);
        testWorkout.setSportTypes(List.of(SportType.BICYCLE));
        testWorkout.setStartDate(Instant.parse("2021-06-19T00:00:00Z"));
        testWorkout.setDescription("description");
        testWorkout.setCreatedAt(community.getCreatedAt());

        final Workout bdWorkout = daoFactory.getWorkoutDao().update(testWorkout);
        assertEquals(1, bdWorkout.getId().intValue());
        assertEquals(1, bdWorkout.getOwnerUserId().intValue());
        assertEquals(false, bdWorkout.getIsPrivate());
        assertEquals(1, bdWorkout.getRouteId().intValue());
        assertEquals(List.of(SportType.BICYCLE), bdWorkout.getSportTypes());
        assertEquals(List.of(SportType.BICYCLE), bdWorkout.getSportTypes());
        assertEquals(Collections.emptyList(), bdWorkout.getUserIds());
        assertEquals(Instant.parse("2021-06-19T00:00:00Z"), bdWorkout.getStartDate());
        assertEquals("description", bdWorkout.getDescription());
        assertEquals(community.getCreatedAt(), bdWorkout.getCreatedAt());
    }
}
