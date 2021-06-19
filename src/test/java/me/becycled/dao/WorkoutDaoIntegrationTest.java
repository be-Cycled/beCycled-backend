package me.becycled.dao;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.community.Community;
import me.becycled.backend.model.entity.route.Route;
import me.becycled.backend.model.entity.route.SportType;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.entity.workout.Workout;
import me.becycled.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Suren Kalaychyan
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WorkoutDaoIntegrationTest extends BaseIntegrationTest {

    @BeforeEach
    public void setUp() {
        daoFactory.getUserDao().create(TestUtils.getTestUser());
        daoFactory.getRouteDao().create(TestUtils.getTestRoute());
        daoFactory.getCommunityDao().create(TestUtils.getTestCommunity());
    }

    @Test
    void create() {
        final Workout testWorkout = TestUtils.getTestWorkout();
        Workout workout = daoFactory.getWorkoutDao().create(testWorkout);
        assertNotNull(workout.getId());
        assertNotNull(workout.getCreatedAt());

        testWorkout.setId(workout.getId());
        testWorkout.setCreatedAt(workout.getCreatedAt());

        assertEquals(testWorkout.getId(), workout.getId());
        assertEquals(testWorkout.getOwnerUserId(), workout.getOwnerUserId());
        assertEquals(testWorkout.getCommunityId(), workout.getCommunityId());
        assertEquals(testWorkout.getPrivate(), workout.getPrivate());
        assertEquals(testWorkout.getStartDate(), workout.getStartDate());
        assertEquals(testWorkout.getRouteId(), workout.getRouteId());
        assertEquals(testWorkout.getSportTypes(), workout.getSportTypes());
        assertEquals(testWorkout.getUserIds(), workout.getUserIds());
        assertEquals(testWorkout.getVenue(), workout.getVenue());
        assertEquals(testWorkout.getDuration(), workout.getDuration());
        assertEquals(testWorkout.getDescription(), workout.getDescription());
        assertEquals(testWorkout.getCreatedAt(), workout.getCreatedAt());
    }

    @Test
    void getByCommunityNickname() {
        final Workout workout = daoFactory.getWorkoutDao().create(TestUtils.getTestWorkout());
        final String nickname = daoFactory.getCommunityDao().getById(1).getNickname();
        final List<Workout> byCommunityNickname = daoFactory.getWorkoutDao().getByCommunityNickname(nickname);

        assertEquals(byCommunityNickname.get(0), workout);
    }

    @Test
    void getById() {
        final Workout workout = daoFactory.getWorkoutDao().create(TestUtils.getTestWorkout());
        final Workout createWorkout = daoFactory.getWorkoutDao().getById(workout.getId());

        assertEquals(createWorkout, workout);
    }

    @Test
    void getAll() {
        final Workout firstWorkout = daoFactory.getWorkoutDao().create(TestUtils.getTestWorkout());
        final Workout secondWorkout = daoFactory.getWorkoutDao().create(TestUtils.getTestWorkout());

        final List<Workout> all = daoFactory.getWorkoutDao().getAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(firstWorkout::equals));
        assertTrue(all.stream().anyMatch(secondWorkout::equals));
    }

    @Test
    void update() {
        User user = TestUtils.getTestUser();
        user.setLogin("1");
        user.setEmail("1@gmail.com");
        user.setPhone("89001600020");
        user = daoFactory.getUserDao().create(user);
        Community community = TestUtils.getTestCommunity();
        community.setNickname("testNickname");
        community = daoFactory.getCommunityDao().create(community);
        final Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());
        final Workout workout = daoFactory.getWorkoutDao().create(TestUtils.getTestWorkout());

        final Workout testWorkout = new Workout();
        testWorkout.setId(workout.getId());
        testWorkout.setOwnerUserId(user.getId());
        testWorkout.setCommunityId(community.getId());
        testWorkout.setPrivate(true);
        testWorkout.setStartDate(Instant.now());
        testWorkout.setRouteId(route.getId());
        testWorkout.setSportTypes(Arrays.asList(SportType.values()));
        testWorkout.setUserIds(List.of(1, 2));
        testWorkout.setVenue("BQ 128");
        testWorkout.setDuration(123);
        testWorkout.setDescription("test");
        testWorkout.setCreatedAt(Instant.now());

        final Workout dbWorkout = daoFactory.getWorkoutDao().update(testWorkout);
        testWorkout.setCreatedAt(dbWorkout.getCreatedAt());

        assertNotNull(dbWorkout.getId());
        assertEquals(testWorkout.getOwnerUserId(), dbWorkout.getOwnerUserId());
        assertEquals(testWorkout.getCommunityId(), dbWorkout.getCommunityId());
        assertEquals(testWorkout.getPrivate(), dbWorkout.getPrivate());
        assertEquals(workout.getRouteId(), dbWorkout.getRouteId());
        assertEquals(testWorkout.getSportTypes(), dbWorkout.getSportTypes());
        assertEquals(testWorkout.getUserIds(), dbWorkout.getUserIds());
        assertEquals(testWorkout.getVenue(), dbWorkout.getVenue());
        assertEquals(testWorkout.getDuration(), dbWorkout.getDuration());
        assertEquals(testWorkout.getDescription(), dbWorkout.getDescription());
        assertEquals(testWorkout.getCreatedAt(), dbWorkout.getCreatedAt());
    }
}
