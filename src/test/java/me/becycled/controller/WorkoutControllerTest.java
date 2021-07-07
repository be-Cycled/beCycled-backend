package me.becycled.controller;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.community.Community;
import me.becycled.backend.model.entity.route.Route;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.entity.workout.Workout;
import me.becycled.backend.service.AccessService;
import me.becycled.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author I1yi4
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WorkoutControllerTest extends BaseIntegrationTest {

    @MockBean
    AccessService accessService;

    //region getById

    @Test
    public void getById() {
        createUser(TestUtils.getTestUser());

        Route route = TestUtils.getTestRoute();
        route = daoFactory.getRouteDao().create(route);

        Workout workout = TestUtils.getTestWorkout();
        workout = daoFactory.getWorkoutDao().create(workout);

        final ResponseEntity<Workout> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/" + workout.getId(),
            HttpMethod.GET, HttpEntity.EMPTY, Workout.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(workout);
    }

    @Test
    public void getByIdWhenNotExist() {
        createUser(TestUtils.getTestUser());

        Route route = TestUtils.getTestRoute();
        route = daoFactory.getRouteDao().create(route);
        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/" + 100500,
            HttpMethod.GET, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Workout is not found");
    }

    //endregion getById

    //region getWorkoutWhichUserMemberByUserLogin

    @Test
    public void getWorkoutWhichUserMemberByUserLogin() {
        User user = createUser(TestUtils.getTestUser());

        Route route = TestUtils.getTestRoute();
        route = daoFactory.getRouteDao().create(route);

        Workout workoutFirst = TestUtils.getTestWorkout();
        workoutFirst.setUserIds(List.of(user.getId()));
        workoutFirst = daoFactory.getWorkoutDao().create(workoutFirst);

        Workout workoutSecond = TestUtils.getTestWorkout();
        workoutSecond.setUserIds(Collections.emptyList());
        workoutSecond = daoFactory.getWorkoutDao().create(workoutSecond);

        Workout workoutThird = TestUtils.getTestWorkout();
        workoutThird.setUserIds(List.of(user.getId()));
        workoutThird = daoFactory.getWorkoutDao().create(workoutThird);

        final ResponseEntity<List<Workout>> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/user/" + user.getLogin(),
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Workout>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(2);
        then(response.getBody().get(0)).isEqualTo(workoutFirst);
        then(response.getBody().get(1)).isEqualTo(workoutThird);
    }

    @Test
    public void getWorkoutWhichUserMemberByUserLoginWhenNoOneWorkout() {
        User user = createUser(TestUtils.getTestUser());

        final ResponseEntity<List<Workout>> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/user/" + user.getLogin(),
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Workout>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(0);
    }

    @Test
    public void getWorkoutWhichUserMemberByUserLoginWhenLoginNotExist() {
        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/user/" + "100500",
            HttpMethod.GET, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("User is not found");
    }

    //endregion getWorkoutWhichUserMemberByUserLogin

    //region getByCommunityNickname

    @Test
    public void getByCommunityNickname() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        Community community = daoFactory.getCommunityDao().create(TestUtils.getTestCommunity());

        Workout workoutFirst = TestUtils.getTestWorkout();
        workoutFirst.setUserIds(List.of(user.getId()));
        workoutFirst.setCommunityId(community.getId());
        workoutFirst = daoFactory.getWorkoutDao().create(workoutFirst);

        Workout workoutSecond = TestUtils.getTestWorkout();
        workoutSecond.setUserIds(Collections.emptyList());
        workoutSecond = daoFactory.getWorkoutDao().create(workoutSecond);

        Workout workoutThird = TestUtils.getTestWorkout();
        workoutThird.setUserIds(List.of(user.getId()));
        workoutThird.setCommunityId(community.getId());
        workoutThird = daoFactory.getWorkoutDao().create(workoutThird);

        final ResponseEntity<List<Workout>> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/community/" + community.getNickname(),
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Workout>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(2);
        then(response.getBody().get(0)).isEqualTo(workoutFirst);
        then(response.getBody().get(1)).isEqualTo(workoutThird);
    }

    @Test
    public void getByCommunityNicknameWhenNotExist() {
        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/community/" + "100500",
            HttpMethod.GET, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Community is not found");
    }

    //endregion getByCommunityNickname

    //region getAll

    @Test
    public void getAll() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        Workout workoutFirst = TestUtils.getTestWorkout();
        workoutFirst = daoFactory.getWorkoutDao().create(workoutFirst);

        Workout workoutSecond = TestUtils.getTestWorkout();
        workoutSecond = daoFactory.getWorkoutDao().create(workoutSecond);

        Workout workoutThird = TestUtils.getTestWorkout();
        workoutThird = daoFactory.getWorkoutDao().create(workoutThird);

        final ResponseEntity<List<Workout>> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/all",
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Workout>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(3);
        then(response.getBody().get(0)).isEqualTo(workoutFirst);
        then(response.getBody().get(1)).isEqualTo(workoutSecond);
        then(response.getBody().get(2)).isEqualTo(workoutThird);
    }

    //endregion getAll

    //region create

    @Test
    public void create() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Workout workout = TestUtils.getTestWorkout();
        workout.setOwnerUserId(100500);

        final RequestEntity<Workout> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/workouts"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(workout);

        final ResponseEntity<Workout> response = restTemplate.exchange(
            request, Workout.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Workout result = response.getBody();
        assertEquals(result.getOwnerUserId(), user.getId());

        workout.setOwnerUserId(user.getId());
        workout.setId(result.getId());
        workout.setCreatedAt(result.getCreatedAt());
        workout.setUserIds(Collections.singletonList(user.getId()));

        then(result).isEqualTo(workout);
    }

    @Test
    public void createWhenOwnerInsideUserIds() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Workout workout = TestUtils.getTestWorkout();
        workout.setUserIds(Collections.singletonList(user.getId()));
        workout.setOwnerUserId(100500);

        final RequestEntity<Workout> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/workouts"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(workout);

        final ResponseEntity<Workout> response = restTemplate.exchange(
            request, Workout.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Workout result = response.getBody();
        assertEquals(result.getOwnerUserId(), user.getId());

        workout.setOwnerUserId(user.getId());
        workout.setId(result.getId());
        workout.setCreatedAt(result.getCreatedAt());
        workout.setUserIds(Collections.singletonList(user.getId()));

        then(result).isEqualTo(workout);
    }

    @Test
    public void createWhenNotAuth() {
        Workout workout = TestUtils.getTestWorkout();

        final RequestEntity<Workout> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/workouts"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(workout);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        then(response.getBody()).isEqualTo("Auth error");
    }

    //endregion create

    //region update

    @Test
    public void update() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Workout workout = TestUtils.getTestWorkout();
        workout.setOwnerUserId(user.getId());
        workout = daoFactory.getWorkoutDao().create(workout);

        workout.setDescription("new");

        final RequestEntity<Workout> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/workouts/" + workout.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(workout);

        final ResponseEntity<Workout> response = restTemplate.exchange(
            request, Workout.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(workout);
    }

    @Test
    public void updateWithDifferentId() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Workout workout = TestUtils.getTestWorkout();
        workout = daoFactory.getWorkoutDao().create(workout);

        final RequestEntity<Workout> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/workouts/" + 100500))
            .contentType(MediaType.APPLICATION_JSON)
            .body(workout);
        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo("Different identifiers in request path and body");
    }

    @Test
    public void updateWhenNotAuth() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        Workout workout = TestUtils.getTestWorkout();
        workout.setOwnerUserId(user.getId());
        workout = daoFactory.getWorkoutDao().create(workout);

        workout.setDescription("new");

        final RequestEntity<Workout> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/workouts/" + workout.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(workout);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        then(response.getBody()).isEqualTo("Auth error");
    }

    @Test
    public void updateWhenNotExist() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Workout workout = TestUtils.getTestWorkout();
        workout.setOwnerUserId(user.getId());
        workout = daoFactory.getWorkoutDao().create(workout);

        workout.setId(100500);
        workout.setDescription("new");

        final RequestEntity<Workout> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/workouts/" + workout.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(workout);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Workout is not found");
    }

    @Test
    public void updateWhenNotOwner() {
        User owner = createUser(TestUtils.getTestUser());
        User user = createUser(prepareUser("test", "test@gmail.com", "89990001122"));

        when(accessService.getCurrentAuthUser()).thenReturn(user);
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Workout workout = TestUtils.getTestWorkout();
        workout.setOwnerUserId(owner.getId());
        workout = daoFactory.getWorkoutDao().create(workout);

        workout.setDescription("new");

        final RequestEntity<Workout> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/workouts/" + workout.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(workout);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo("Workout can be updated by owner only");
    }

    //endregion update

    //region delete

    @Test
    public void delete() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Workout workout = TestUtils.getTestWorkout();
        workout = daoFactory.getWorkoutDao().create(workout);

        final ResponseEntity<Integer> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/" + workout.getId(),
            HttpMethod.DELETE, HttpEntity.EMPTY, Integer.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(1);
    }

    @Test
    public void deleteWhenNotAuth() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        Workout workout = TestUtils.getTestWorkout();
        workout = daoFactory.getWorkoutDao().create(workout);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/" + workout.getId(),
            HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        then(response.getBody()).isEqualTo("Auth error");
    }

    @Test
    public void deleteWhenNotExist() {
        User user = createUser(TestUtils.getTestUser());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/" + 100500,
            HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Workout is not found");
    }

    @Test
    public void deleteWhenNotOwner() {
        User owner = createUser(TestUtils.getTestUser());
        User user = createUser(prepareUser("test", "test@gmail.com", "89990001122"));
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Workout workout = TestUtils.getTestWorkout();
        workout.setOwnerUserId(owner.getId());
        workout = daoFactory.getWorkoutDao().create(workout);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/" + workout.getId(),
            HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo("Workout can be deleted by owner only");
    }

    //endregion delete

    //region join

    @Test
    public void join() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        User firstUser = createUser(prepareUser("test0", "test0@gmail.com", "88005553503"));
        assertNotNull(firstUser.getId());

        User secondUser = createUser(prepareUser("test", "test@gmail.com", "88005553530"));
        assertNotNull(secondUser.getId());

        User thirdUser = createUser(prepareUser("test1", "test1@gmail.com", "88005553531"));
        assertNotNull(thirdUser.getId());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Workout workout = TestUtils.getTestWorkout();
        workout.setUserIds(List.of(firstUser.getId(), secondUser.getId(), thirdUser.getId()));
        workout = daoFactory.getWorkoutDao().create(workout);

        final ResponseEntity<Workout> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/join/" + workout.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, Workout.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        workout.setUserIds(List.of(firstUser.getId(), secondUser.getId(), thirdUser.getId(), user.getId()));
        then(response.getBody()).isEqualTo(workout);
    }

    @Test
    public void joinWhenNoOneInWorkout() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Workout workout = TestUtils.getTestWorkout();
        workout.setUserIds(Collections.emptyList());
        workout = daoFactory.getWorkoutDao().create(workout);

        final ResponseEntity<Workout> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/join/" + workout.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, Workout.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        workout.setUserIds(Collections.singletonList(user.getId()));

        then(response.getBody()).isEqualTo(workout);
    }

    @Test
    public void joinWhenNotAuth() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        Workout workout = TestUtils.getTestWorkout();
        workout = daoFactory.getWorkoutDao().create(workout);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/join/" + workout.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        then(response.getBody()).isEqualTo("Auth error");
    }

    @Test
    public void joinCommunityNotExist() {
        User user = createUser(TestUtils.getTestUser());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/join/" + 100500,
            HttpMethod.POST, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Workout is not found");
    }

    @Test
    public void joinWhenAlreadyJoined() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Workout workout = TestUtils.getTestWorkout();
        workout.setUserIds(Collections.singletonList(user.getId()));
        workout = daoFactory.getWorkoutDao().create(workout);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/join/" + workout.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo("Current user is already joined");
    }

    //endregion join

    //region leave

    @Test
    public void leave() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        User firstUser = createUser(prepareUser("test0", "test0@gmail.com", "88005553503"));
        assertNotNull(firstUser.getId());

        User secondUser = createUser(prepareUser("test", "test@gmail.com", "88005553530"));
        assertNotNull(secondUser.getId());

        User thirdUser = createUser(prepareUser("test1", "test1@gmail.com", "88005553531"));
        assertNotNull(thirdUser.getId());
        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Workout workout = TestUtils.getTestWorkout();
        workout.setUserIds(List.of(firstUser.getId(), secondUser.getId(), thirdUser.getId(), user.getId()));
        workout = daoFactory.getWorkoutDao().create(workout);

        final ResponseEntity<Workout> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/leave/" + workout.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, Workout.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        workout.setUserIds(List.of(firstUser.getId(), secondUser.getId(), thirdUser.getId()));
        then(response.getBody()).isEqualTo(workout);
    }

    @Test
    public void leaveWhenNotAuth() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        Workout workout = TestUtils.getTestWorkout();
        workout = daoFactory.getWorkoutDao().create(workout);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/leave/" + workout.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        then(response.getBody()).isEqualTo("Auth error");
    }

    @Test
    public void leaveWhenCommunityNotExist() {
        User user = createUser(TestUtils.getTestUser());
        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/leave/" + 100500,
            HttpMethod.POST, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Workout is not found");
    }

    @Test
    public void leaveWhenNotJoined() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        User firstUser = createUser(prepareUser("test0", "test0@gmail.com", "88005553503"));
        assertNotNull(firstUser.getId());

        User secondUser = createUser(prepareUser("test", "test@gmail.com", "88005553530"));
        assertNotNull(secondUser.getId());

        User thirdUser = createUser(prepareUser("test1", "test1@gmail.com", "88005553531"));
        assertNotNull(thirdUser.getId());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Workout workout = TestUtils.getTestWorkout();
        workout.setUserIds(List.of(firstUser.getId(), secondUser.getId(), thirdUser.getId()));

        workout = daoFactory.getWorkoutDao().create(workout);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/workouts/leave/" + workout.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo("Current user is not joined");
    }

    //endregion leave

    private User prepareUser(final String login, String email, String phone) {
        User secondUser = TestUtils.getTestUser();
        secondUser.setLogin(login);
        secondUser.setEmail(email);
        secondUser.setPhone(phone);
        return secondUser;
    }
}
