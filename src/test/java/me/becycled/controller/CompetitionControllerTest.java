package me.becycled.controller;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.community.Community;
import me.becycled.backend.model.entity.competition.Competition;
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
public class CompetitionControllerTest extends BaseIntegrationTest {

    @MockBean
    AccessService accessService;

    //region getById

    @Test
    public void getById() {
        createUser(TestUtils.getTestUser());

        Route route = TestUtils.getTestRoute();
        route = daoFactory.getRouteDao().create(route);

        Competition competition = TestUtils.getTestCompetition();
        competition = daoFactory.getCompetitionDao().create(competition);

        final ResponseEntity<Competition> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/" + competition.getId(),
            HttpMethod.GET, HttpEntity.EMPTY, Competition.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(competition);
    }

    @Test
    public void getByIdWhenNotExist() {
        createUser(TestUtils.getTestUser());

        Route route = TestUtils.getTestRoute();
        route = daoFactory.getRouteDao().create(route);
        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/" + 100500,
            HttpMethod.GET, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Competition is not found");
    }

    //endregion getById

    //region getCompetitionWhichUserMemberByUserLogin

    @Test
    public void getCompetitionWhichUserMemberByUserLogin() {
        User user = createUser(TestUtils.getTestUser());

        Route route = TestUtils.getTestRoute();
        route = daoFactory.getRouteDao().create(route);

        Competition competitionFirst = TestUtils.getTestCompetition();
        competitionFirst.setUserIds(List.of(user.getId()));
        competitionFirst = daoFactory.getCompetitionDao().create(competitionFirst);

        Competition competitionSecond = TestUtils.getTestCompetition();
        competitionSecond.setUserIds(Collections.emptyList());
        competitionSecond = daoFactory.getCompetitionDao().create(competitionSecond);

        Competition competitionThird = TestUtils.getTestCompetition();
        competitionThird.setUserIds(List.of(user.getId()));
        competitionThird = daoFactory.getCompetitionDao().create(competitionThird);

        final ResponseEntity<List<Competition>> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/user/" + user.getLogin(),
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Competition>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(2);
        then(response.getBody().get(0)).isEqualTo(competitionFirst);
        then(response.getBody().get(1)).isEqualTo(competitionThird);
    }

    @Test
    public void getCompetitionWhichUserMemberByUserLoginWhenNoOneCompetition() {
        User user = createUser(TestUtils.getTestUser());

        final ResponseEntity<List<Competition>> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/user/" + user.getLogin(),
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Competition>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(0);
    }

    @Test
    public void getCompetitionWhichUserMemberByUserLoginWhenLoginNotExist() {
        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/user/" + "100500",
            HttpMethod.GET, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("User is not found");
    }

    //endregion getCompetitionWhichUserMemberByUserLogin

    //region getByCommunityNickname

    @Test
    public void getByCommunityNickname() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        Community community = daoFactory.getCommunityDao().create(TestUtils.getTestCommunity());

        Competition competitionFirst = TestUtils.getTestCompetition();
        competitionFirst.setUserIds(List.of(user.getId()));
        competitionFirst.setCommunityId(community.getId());
        competitionFirst = daoFactory.getCompetitionDao().create(competitionFirst);

        Competition competitionSecond = TestUtils.getTestCompetition();
        competitionSecond.setUserIds(Collections.emptyList());
        competitionSecond = daoFactory.getCompetitionDao().create(competitionSecond);

        Competition competitionThird = TestUtils.getTestCompetition();
        competitionThird.setUserIds(List.of(user.getId()));
        competitionThird.setCommunityId(community.getId());
        competitionThird = daoFactory.getCompetitionDao().create(competitionThird);

        final ResponseEntity<List<Competition>> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/community/" + community.getNickname(),
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Competition>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(2);
        then(response.getBody().get(0)).isEqualTo(competitionFirst);
        then(response.getBody().get(1)).isEqualTo(competitionThird);
    }

    @Test
    public void getByCommunityNicknameWhenNotExist() {
        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/community/" + "100500",
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

        Competition competitionFirst = TestUtils.getTestCompetition();
        competitionFirst = daoFactory.getCompetitionDao().create(competitionFirst);

        Competition competitionSecond = TestUtils.getTestCompetition();
        competitionSecond = daoFactory.getCompetitionDao().create(competitionSecond);

        Competition competitionThird = TestUtils.getTestCompetition();
        competitionThird = daoFactory.getCompetitionDao().create(competitionThird);

        final ResponseEntity<List<Competition>> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/all",
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Competition>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(3);
        then(response.getBody().get(0)).isEqualTo(competitionFirst);
        then(response.getBody().get(1)).isEqualTo(competitionSecond);
        then(response.getBody().get(2)).isEqualTo(competitionThird);
    }

    //endregion getAll

    //region create

    @Test
    public void create() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Competition competition = TestUtils.getTestCompetition();
        competition.setOwnerUserId(100500);

        final RequestEntity<Competition> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/competitions"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(competition);

        final ResponseEntity<Competition> response = restTemplate.exchange(
            request, Competition.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Competition result = response.getBody();
        assertEquals(result.getOwnerUserId(), user.getId());

        competition.setOwnerUserId(user.getId());
        competition.setId(result.getId());
        competition.setCreatedAt(result.getCreatedAt());
        competition.setUserIds(Collections.singletonList(user.getId()));

        then(result).isEqualTo(competition);
    }

    @Test
    public void createWhenOwnerInsideUserIds() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Competition competition = TestUtils.getTestCompetition();
        competition.setUserIds(Collections.singletonList(user.getId()));
        competition.setOwnerUserId(100500);

        final RequestEntity<Competition> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/competitions"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(competition);

        final ResponseEntity<Competition> response = restTemplate.exchange(
            request, Competition.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Competition result = response.getBody();
        assertEquals(result.getOwnerUserId(), user.getId());

        competition.setOwnerUserId(user.getId());
        competition.setId(result.getId());
        competition.setCreatedAt(result.getCreatedAt());
        competition.setUserIds(Collections.singletonList(user.getId()));

        then(result).isEqualTo(competition);
    }

    @Test
    public void createWhenNotAuth() {
        Competition competition = TestUtils.getTestCompetition();

        final RequestEntity<Competition> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/competitions"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(competition);

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

        Competition competition = TestUtils.getTestCompetition();
        competition.setOwnerUserId(user.getId());
        competition = daoFactory.getCompetitionDao().create(competition);

        competition.setDescription("new");

        final RequestEntity<Competition> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/competitions/" + competition.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(competition);

        final ResponseEntity<Competition> response = restTemplate.exchange(
            request, Competition.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(competition);
    }

    @Test
    public void updateWithDifferentId() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Competition competition = TestUtils.getTestCompetition();
        competition = daoFactory.getCompetitionDao().create(competition);

        final RequestEntity<Competition> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/competitions/" + 100500))
            .contentType(MediaType.APPLICATION_JSON)
            .body(competition);
        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo("Different identifiers in request path and body");
    }

    @Test
    public void updateWhenNotAuth() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        Competition competition = TestUtils.getTestCompetition();
        competition.setOwnerUserId(user.getId());
        competition = daoFactory.getCompetitionDao().create(competition);

        competition.setDescription("new");

        final RequestEntity<Competition> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/competitions/" + competition.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(competition);

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

        Competition competition = TestUtils.getTestCompetition();
        competition.setOwnerUserId(user.getId());
        competition = daoFactory.getCompetitionDao().create(competition);

        competition.setId(100500);
        competition.setDescription("new");

        final RequestEntity<Competition> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/competitions/" + competition.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(competition);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Competition is not found");
    }

    @Test
    public void updateWhenNotOwner() {
        User owner = createUser(TestUtils.getTestUser());
        User user = createUser(prepareUser("test", "test@gmail.com", "89990001122"));

        when(accessService.getCurrentAuthUser()).thenReturn(user);
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Competition competition = TestUtils.getTestCompetition();
        competition.setOwnerUserId(owner.getId());
        competition = daoFactory.getCompetitionDao().create(competition);

        competition.setDescription("new");

        final RequestEntity<Competition> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/competitions/" + competition.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(competition);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo("Competition can be updated by owner only");
    }

    //endregion update

    //region delete

    @Test
    public void delete() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Competition competition = TestUtils.getTestCompetition();
        competition = daoFactory.getCompetitionDao().create(competition);

        final ResponseEntity<Integer> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/" + competition.getId(),
            HttpMethod.DELETE, HttpEntity.EMPTY, Integer.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(1);
    }

    @Test
    public void deleteWhenNotAuth() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        Competition competition = TestUtils.getTestCompetition();
        competition = daoFactory.getCompetitionDao().create(competition);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/" + competition.getId(),
            HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        then(response.getBody()).isEqualTo("Auth error");
    }

    @Test
    public void deleteWhenNotExist() {
        User user = createUser(TestUtils.getTestUser());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/" + 100500,
            HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Competition is not found");
    }

    @Test
    public void deleteWhenNotOwner() {
        User owner = createUser(TestUtils.getTestUser());
        User user = createUser(prepareUser("test", "test@gmail.com", "89990001122"));
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Competition competition = TestUtils.getTestCompetition();
        competition.setOwnerUserId(owner.getId());
        competition = daoFactory.getCompetitionDao().create(competition);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/" + competition.getId(),
            HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo("Competition can be deleted by owner only");
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

        Competition competition = TestUtils.getTestCompetition();
        competition.setUserIds(List.of(firstUser.getId(), secondUser.getId(), thirdUser.getId()));
        competition = daoFactory.getCompetitionDao().create(competition);

        final ResponseEntity<Competition> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/join/" + competition.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, Competition.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        competition.setUserIds(List.of(firstUser.getId(), secondUser.getId(), thirdUser.getId(), user.getId()));
        then(response.getBody()).isEqualTo(competition);
    }

    @Test
    public void joinWhenNoOneInCompetition() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Competition competition = TestUtils.getTestCompetition();
        competition.setUserIds(Collections.emptyList());
        competition = daoFactory.getCompetitionDao().create(competition);

        final ResponseEntity<Competition> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/join/" + competition.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, Competition.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        competition.setUserIds(Collections.singletonList(user.getId()));

        then(response.getBody()).isEqualTo(competition);
    }

    @Test
    public void joinWhenNotAuth() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        Competition competition = TestUtils.getTestCompetition();
        competition = daoFactory.getCompetitionDao().create(competition);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/join/" + competition.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        then(response.getBody()).isEqualTo("Auth error");
    }

    @Test
    public void joinCommunityNotExist() {
        User user = createUser(TestUtils.getTestUser());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/join/" + 100500,
            HttpMethod.POST, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Competition is not found");
    }

    @Test
    public void joinWhenAlreadyJoined() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Competition competition = TestUtils.getTestCompetition();
        competition.setUserIds(Collections.singletonList(user.getId()));
        competition = daoFactory.getCompetitionDao().create(competition);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/join/" + competition.getId(),
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

        Competition competition = TestUtils.getTestCompetition();
        competition.setUserIds(List.of(firstUser.getId(), secondUser.getId(), thirdUser.getId(), user.getId()));
        competition = daoFactory.getCompetitionDao().create(competition);

        final ResponseEntity<Competition> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/leave/" + competition.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, Competition.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        competition.setUserIds(List.of(firstUser.getId(), secondUser.getId(), thirdUser.getId()));
        then(response.getBody()).isEqualTo(competition);
    }

    @Test
    public void leaveWhenNotAuth() {
        User user = createUser(TestUtils.getTestUser());
        Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        Competition competition = TestUtils.getTestCompetition();
        competition = daoFactory.getCompetitionDao().create(competition);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/leave/" + competition.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        then(response.getBody()).isEqualTo("Auth error");
    }

    @Test
    public void leaveWhenCommunityNotExist() {
        User user = createUser(TestUtils.getTestUser());
        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/leave/" + 100500,
            HttpMethod.POST, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Competition is not found");
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

        Competition competition = TestUtils.getTestCompetition();
        competition.setUserIds(List.of(firstUser.getId(), secondUser.getId(), thirdUser.getId()));

        competition = daoFactory.getCompetitionDao().create(competition);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/competitions/leave/" + competition.getId(),
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
