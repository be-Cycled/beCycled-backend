package me.becycled.controller;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.community.Community;
import me.becycled.backend.model.entity.event.Event;
import me.becycled.backend.model.entity.route.Route;
import me.becycled.backend.model.entity.user.User;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
public class EventControllerTest extends BaseIntegrationTest {

    @MockBean
    AccessService accessService;

    //region getById

    @Test
    public void getById() {
        createUser(TestUtils.getTestUser());

        Route route = TestUtils.getTestRoute();
        route = daoFactory.getRouteDao().create(route);

        Event event = TestUtils.getTestEvent();
        event = daoFactory.getEventDao().create(event);

        final ResponseEntity<Event> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/" + event.getId(),
            HttpMethod.GET, HttpEntity.EMPTY, Event.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(event);
    }

    @Test
    public void getByIdWhenNotExist() {
        createUser(TestUtils.getTestUser());

        Route route = TestUtils.getTestRoute();
        route = daoFactory.getRouteDao().create(route);
        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/" + 100500,
            HttpMethod.GET, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Event is not found");
    }

    //endregion getById

    //region getWorkoutWhichUserMemberByUserLogin

    @Test
    public void getWorkoutWhichUserMemberByUserLogin() {
        User user = createUser(TestUtils.getTestUser());

        Route route = TestUtils.getTestRoute();
        route = daoFactory.getRouteDao().create(route);

        Event eventFirst = TestUtils.getTestEvent();
        eventFirst.setMemberUserIds(List.of(user.getId()));
        eventFirst = daoFactory.getEventDao().create(eventFirst);

        Event eventSecond = TestUtils.getTestEvent();
        eventSecond.setMemberUserIds(Collections.emptyList());
        eventSecond = daoFactory.getEventDao().create(eventSecond);

        Event eventThird = TestUtils.getTestEvent();
        eventThird.setMemberUserIds(List.of(user.getId()));
        eventThird = daoFactory.getEventDao().create(eventThird);

        final ResponseEntity<List<Event>> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/user/" + user.getLogin(),
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Event>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(2);
        then(response.getBody().get(0)).isEqualTo(eventFirst);
        then(response.getBody().get(1)).isEqualTo(eventThird);
    }

    @Test
    public void getWorkoutWhichUserMemberByUserLoginWhenNoOneWorkout() {
        User user = createUser(TestUtils.getTestUser());

        final ResponseEntity<List<Event>> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/user/" + user.getLogin(),
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Event>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(0);
    }

    @Test
    public void getWorkoutWhichUserMemberByUserLoginWhenLoginNotExist() {
        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/user/" + "100500",
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

        Event eventFirst = TestUtils.getTestEvent();
        eventFirst.setMemberUserIds(List.of(user.getId()));
        eventFirst.setCommunityId(community.getId());
        eventFirst = daoFactory.getEventDao().create(eventFirst);

        Event eventSecond = TestUtils.getTestEvent();
        eventSecond.setMemberUserIds(Collections.emptyList());
        eventSecond = daoFactory.getEventDao().create(eventSecond);

        Event eventThird = TestUtils.getTestEvent();
        eventThird.setMemberUserIds(List.of(user.getId()));
        eventThird.setCommunityId(community.getId());
        eventThird = daoFactory.getEventDao().create(eventThird);

        final ResponseEntity<List<Event>> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/community/" + community.getNickname(),
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Event>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(2);
        then(response.getBody().get(0)).isEqualTo(eventFirst);
        then(response.getBody().get(1)).isEqualTo(eventThird);
    }

    @Test
    public void getByCommunityNicknameWhenNotExist() {
        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/community/" + "100500",
            HttpMethod.GET, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Community is not found");
    }

    //endregion getByCommunityNickname

    //region getAffiche

    @Test
    public void getAffiche() {
        User firstUser = createUser(prepareUser("test0", "test0@gmail.com", "88005553503"));
        User secondUser = createUser(prepareUser("test", "test@gmail.com", "88005553530"));
        User thirdUser = createUser(prepareUser("test1", "test1@gmail.com", "88005553531"));
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        final Instant now = Instant.now();

        Event eventFirst = TestUtils.getTestEvent();
        eventFirst.setMemberUserIds(List.of(1, 2, 3));
        eventFirst.setStartDate(now.plus(1, ChronoUnit.HOURS));
        eventFirst.setDuration(1800);
        eventFirst = daoFactory.getEventDao().create(eventFirst);

        Event eventSecond = TestUtils.getTestEvent();
        eventSecond.setMemberUserIds(List.of(3));
        eventSecond.setStartDate(now.minus(1, ChronoUnit.HOURS));
        eventSecond.setDuration(1800);
        eventSecond = daoFactory.getEventDao().create(eventSecond);

        Event eventThird = TestUtils.getTestEvent();
        eventThird.setMemberUserIds(Collections.emptyList());
        eventThird.setStartDate(now.plus(240, ChronoUnit.HOURS));
        eventThird.setDuration(1800);
        eventThird = daoFactory.getEventDao().create(eventThird);

        final ResponseEntity<List<Event>> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/affiche",
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Event>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(2);
        then(response.getBody().get(0)).isEqualTo(eventFirst);
        then(response.getBody().get(1)).isEqualTo(eventThird);
    }

    //endregion getAffiche

    //region getFeed

    @Test
    public void getFeed() {
        User firstUser = createUser(prepareUser("test0", "test0@gmail.com", "88005553503"));
        User secondUser = createUser(prepareUser("test", "test@gmail.com", "88005553530"));
        User thirdUser = createUser(prepareUser("test1", "test1@gmail.com", "88005553531"));
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        final Instant now = Instant.now();

        Event eventFirst = TestUtils.getTestEvent();
        eventFirst.setMemberUserIds(List.of(1, 2, 3));
        eventFirst.setStartDate(now.plus(1, ChronoUnit.HOURS));
        eventFirst.setDuration(1800);
        eventFirst = daoFactory.getEventDao().create(eventFirst);

        Event eventSecond = TestUtils.getTestEvent();
        eventSecond.setMemberUserIds(List.of(3));
        eventSecond.setStartDate(now.minus(1, ChronoUnit.HOURS));
        eventSecond.setDuration(1800);
        eventSecond = daoFactory.getEventDao().create(eventSecond);

        Event eventThird = TestUtils.getTestEvent();
        eventThird.setMemberUserIds(Collections.emptyList());
        eventThird.setStartDate(now.plus(240, ChronoUnit.HOURS));
        eventThird.setDuration(1800);
        eventThird = daoFactory.getEventDao().create(eventThird);

        final ResponseEntity<List<Event>> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/feed",
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Event>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(1);
        then(response.getBody().get(0)).isEqualTo(eventSecond);
    }

    //endregion getFeed

    //region getAll

    @Test
    public void getAll() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        Event eventFirst = TestUtils.getTestEvent();
        eventFirst = daoFactory.getEventDao().create(eventFirst);

        Event eventSecond = TestUtils.getTestEvent();
        eventSecond = daoFactory.getEventDao().create(eventSecond);

        Event eventThird = TestUtils.getTestEvent();
        eventThird = daoFactory.getEventDao().create(eventThird);

        final ResponseEntity<List<Event>> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/all",
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Event>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(3);
        then(response.getBody().get(0)).isEqualTo(eventFirst);
        then(response.getBody().get(1)).isEqualTo(eventSecond);
        then(response.getBody().get(2)).isEqualTo(eventThird);
    }

    //endregion getAll

    //region create

    @Test
    public void create() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Event event = TestUtils.getTestEvent();
        event.setOwnerUserId(100500);

        final RequestEntity<Event> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/events"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(event);

        final ResponseEntity<Event> response = restTemplate.exchange(
            request, Event.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Event result = response.getBody();
        assertEquals(result.getOwnerUserId(), user.getId());

        event.setOwnerUserId(user.getId());
        event.setId(result.getId());
        event.setCreatedAt(result.getCreatedAt());
        event.setMemberUserIds(Collections.singletonList(user.getId()));

        then(result).isEqualTo(event);
    }

    @Test
    public void createWhenOwnerInsideUserIds() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Event event = TestUtils.getTestEvent();
        event.setMemberUserIds(Collections.singletonList(user.getId()));
        event.setOwnerUserId(100500);

        final RequestEntity<Event> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/events"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(event);

        final ResponseEntity<Event> response = restTemplate.exchange(
            request, Event.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Event result = response.getBody();
        assertEquals(result.getOwnerUserId(), user.getId());

        event.setOwnerUserId(user.getId());
        event.setId(result.getId());
        event.setCreatedAt(result.getCreatedAt());
        event.setMemberUserIds(Collections.singletonList(user.getId()));

        then(result).isEqualTo(event);
    }

    @Test
    public void createWhenNotAuth() {
        Event event = TestUtils.getTestEvent();

        final RequestEntity<Event> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/events"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(event);

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

        Event event = TestUtils.getTestEvent();
        event.setOwnerUserId(user.getId());
        event = daoFactory.getEventDao().create(event);

        event.setDescription("new");

        final RequestEntity<Event> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/events/" + event.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(event);

        final ResponseEntity<Event> response = restTemplate.exchange(
            request, Event.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(event);
    }

    @Test
    public void updateWithDifferentId() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Event event = TestUtils.getTestEvent();
        event = daoFactory.getEventDao().create(event);

        final RequestEntity<Event> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/events/" + 100500))
            .contentType(MediaType.APPLICATION_JSON)
            .body(event);
        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo("Different identifiers in request path and body");
    }

    @Test
    public void updateWhenNotAuth() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        Event event = TestUtils.getTestEvent();
        event.setOwnerUserId(user.getId());
        event = daoFactory.getEventDao().create(event);

        event.setDescription("new");

        final RequestEntity<Event> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/events/" + event.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(event);

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

        Event event = TestUtils.getTestEvent();
        event.setOwnerUserId(user.getId());
        event = daoFactory.getEventDao().create(event);

        event.setId(100500);
        event.setDescription("new");

        final RequestEntity<Event> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/events/" + event.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(event);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Event is not found");
    }

    @Test
    public void updateWhenNotOwner() {
        User owner = createUser(TestUtils.getTestUser());
        User user = createUser(prepareUser("test", "test@gmail.com", "89990001122"));

        when(accessService.getCurrentAuthUser()).thenReturn(user);
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Event event = TestUtils.getTestEvent();
        event.setOwnerUserId(owner.getId());
        event = daoFactory.getEventDao().create(event);

        event.setDescription("new");

        final RequestEntity<Event> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/events/" + event.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(event);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo("Event can be updated by owner only");
    }

    //endregion update

    //region delete

    @Test
    public void delete() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Event event = TestUtils.getTestEvent();
        event = daoFactory.getEventDao().create(event);

        final ResponseEntity<Integer> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/" + event.getId(),
            HttpMethod.DELETE, HttpEntity.EMPTY, Integer.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(1);
    }

    @Test
    public void deleteWhenNotAuth() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        Event event = TestUtils.getTestEvent();
        event = daoFactory.getEventDao().create(event);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/" + event.getId(),
            HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        then(response.getBody()).isEqualTo("Auth error");
    }

    @Test
    public void deleteWhenNotExist() {
        User user = createUser(TestUtils.getTestUser());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/" + 100500,
            HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Event is not found");
    }

    @Test
    public void deleteWhenNotOwner() {
        User owner = createUser(TestUtils.getTestUser());
        User user = createUser(prepareUser("test", "test@gmail.com", "89990001122"));
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Event event = TestUtils.getTestEvent();
        event.setOwnerUserId(owner.getId());
        event = daoFactory.getEventDao().create(event);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/" + event.getId(),
            HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo("Event can be deleted by owner only");
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

        Event event = TestUtils.getTestEvent();
        event.setMemberUserIds(List.of(firstUser.getId(), secondUser.getId(), thirdUser.getId()));
        event = daoFactory.getEventDao().create(event);

        final ResponseEntity<Event> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/join/" + event.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, Event.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        event.setMemberUserIds(List.of(firstUser.getId(), secondUser.getId(), thirdUser.getId(), user.getId()));
        then(response.getBody()).isEqualTo(event);
    }

    @Test
    public void joinWhenNoOneInWorkout() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Event event = TestUtils.getTestEvent();
        event.setMemberUserIds(Collections.emptyList());
        event = daoFactory.getEventDao().create(event);

        final ResponseEntity<Event> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/join/" + event.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, Event.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        event.setMemberUserIds(Collections.singletonList(user.getId()));

        then(response.getBody()).isEqualTo(event);
    }

    @Test
    public void joinWhenNotAuth() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        Event event = TestUtils.getTestEvent();
        event = daoFactory.getEventDao().create(event);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/join/" + event.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        then(response.getBody()).isEqualTo("Auth error");
    }

    @Test
    public void joinCommunityNotExist() {
        User user = createUser(TestUtils.getTestUser());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/join/" + 100500,
            HttpMethod.POST, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Event is not found");
    }

    @Test
    public void joinWhenAlreadyJoined() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Event event = TestUtils.getTestEvent();
        event.setMemberUserIds(Collections.singletonList(user.getId()));
        event = daoFactory.getEventDao().create(event);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/join/" + event.getId(),
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

        Event event = TestUtils.getTestEvent();
        event.setMemberUserIds(List.of(firstUser.getId(), secondUser.getId(), thirdUser.getId(), user.getId()));
        event = daoFactory.getEventDao().create(event);

        final ResponseEntity<Event> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/leave/" + event.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, Event.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        event.setMemberUserIds(List.of(firstUser.getId(), secondUser.getId(), thirdUser.getId()));
        then(response.getBody()).isEqualTo(event);
    }

    @Test
    public void leaveWhenNotAuth() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        Event event = TestUtils.getTestEvent();
        event = daoFactory.getEventDao().create(event);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/leave/" + event.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        then(response.getBody()).isEqualTo("Auth error");
    }

    @Test
    public void leaveWhenCommunityNotExist() {
        User user = createUser(TestUtils.getTestUser());
        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/leave/" + 100500,
            HttpMethod.POST, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Event is not found");
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

        Event event = TestUtils.getTestEvent();
        event.setMemberUserIds(List.of(firstUser.getId(), secondUser.getId(), thirdUser.getId()));

        event = daoFactory.getEventDao().create(event);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/events/leave/" + event.getId(),
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
