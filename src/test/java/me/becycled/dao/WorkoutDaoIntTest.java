package me.becycled.dao;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.community.Community;
import me.becycled.backend.model.entity.route.Route;
import me.becycled.backend.model.entity.route.SportType;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.entity.user.UserAccount;
import me.becycled.backend.model.entity.workout.Workout;
import me.becycled.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Suren Kalaychyan
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WorkoutDaoIntTest extends BaseIntegrationTest {

    @BeforeEach
    void setUp() {
        daoFactory.getUserAccountDao().create(TestUtils.getTestUser(), TestUtils.getTestUserAccount());
        daoFactory.getRouteDao().create(TestUtils.getTestRoute());
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
        assertEquals(testWorkout.getSportType(), workout.getSportType());
        assertEquals(testWorkout.getUserIds(), workout.getUserIds());
        assertEquals(testWorkout.getVenue(), workout.getVenue());
        assertEquals(testWorkout.getDuration(), workout.getDuration());
        assertEquals(testWorkout.getDescription(), workout.getDescription());
        assertEquals(testWorkout.getCreatedAt(), workout.getCreatedAt());
    }

    @Test
    void getByCommunityNickname() {
        daoFactory.getCommunityDao().create(TestUtils.getTestCommunity());

        final Workout testWorkout = TestUtils.getTestWorkout();
        testWorkout.setCommunityId(1);
        final Workout workout = daoFactory.getWorkoutDao().create(testWorkout);
        final String nickname = daoFactory.getCommunityDao().getById(testWorkout.getId()).getNickname();
        final List<Workout> bdWorkout = daoFactory.getWorkoutDao().getByCommunityNickname(nickname);

        assertEquals(1, bdWorkout.size());
        assertEquals(workout, bdWorkout.get(0));
    }

    @Test
    void delete() {
        final Workout competition = daoFactory.getWorkoutDao().create(TestUtils.getTestWorkout());

        final Workout createdWorkout = daoFactory.getWorkoutDao().getById(competition.getId());
        assertEquals(createdWorkout, competition);

        int delete = daoFactory.getWorkoutDao().delete(createdWorkout.getId());
        assertEquals(1, delete);

        final Workout afterDelete = daoFactory.getWorkoutDao().getById(competition.getId());
        assertNull(afterDelete);

        int deleteNotExist = daoFactory.getWorkoutDao().delete(100500);
        assertEquals(0, deleteNotExist);
    }

    @Test
    void getById() {
        final Workout workout = daoFactory.getWorkoutDao().create(TestUtils.getTestWorkout());
        final Workout createWorkout = daoFactory.getWorkoutDao().getById(workout.getId());

        assertEquals(createWorkout, workout);
    }

    @Test
    void getByMemberUserId() {
        final User user = new User();
        user.setLogin("login1");
        user.setEmail("email1@gmail.com");
        daoFactory.getUserAccountDao().create(user, TestUtils.getTestUserAccount());
        user.setLogin("login2");
        user.setEmail("email2@gmail.com");
        daoFactory.getUserAccountDao().create(user, TestUtils.getTestUserAccount());

        Workout workoutFirst = TestUtils.getTestWorkout();
        workoutFirst.setUserIds(List.of(1, 2, 3));
        workoutFirst = daoFactory.getWorkoutDao().create(workoutFirst);

        Workout workoutSecond = TestUtils.getTestWorkout();
        workoutSecond.setUserIds(List.of(3));
        workoutSecond = daoFactory.getWorkoutDao().create(workoutSecond);

        Workout workoutThird = TestUtils.getTestWorkout();
        workoutThird.setUserIds(Collections.emptyList());
        workoutThird = daoFactory.getWorkoutDao().create(workoutThird);

        final List<Workout> result = daoFactory.getWorkoutDao().getByMemberUserId(3);
        assertEquals(2, result.size());
        assertEquals(workoutFirst, result.get(0));
        assertEquals(workoutSecond, result.get(1));
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
        UserAccount userAccount = daoFactory.getUserAccountDao().create(user, TestUtils.getTestUserAccount());
        user = daoFactory.getUserDao().getById(userAccount.getUserId());
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
        testWorkout.setSportType(SportType.BICYCLE);
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
        assertEquals(testWorkout.getSportType(), dbWorkout.getSportType());
        assertEquals(testWorkout.getUserIds(), dbWorkout.getUserIds());
        assertEquals(testWorkout.getVenue(), dbWorkout.getVenue());
        assertEquals(testWorkout.getDuration(), dbWorkout.getDuration());
        assertEquals(testWorkout.getDescription(), dbWorkout.getDescription());
        assertEquals(testWorkout.getCreatedAt(), dbWorkout.getCreatedAt());
    }
}
