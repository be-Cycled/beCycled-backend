package me.becycled.backend.controller;

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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author binakot
 * @author I1yi4
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest extends BaseIntegrationTest {

    @MockBean
    AccessService accessService;

    @Test
    public void getMe() {
        User user = createUser(TestUtils.getTestUser());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final ResponseEntity<User> response = restTemplate.exchange(
            "http://localhost:" + port + "/users/me",
            HttpMethod.GET, HttpEntity.EMPTY, User.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(user);
    }

    @Test
    public void getMeWhenNotAuth() {
        User user = createUser(TestUtils.getTestUser());

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/users/me",
            HttpMethod.GET, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        then(response.getBody()).isEqualTo("Auth error");
    }

    @Test
    public void getByIdAnotherUser() {
        User curUser = createUser(TestUtils.getTestUser());
        when(accessService.getCurrentAuthUser()).thenReturn(curUser);

        User anotherUser = createUser(prepareUser("test0", "test0@gmail.com", "88005553503"));
        assertNotNull(anotherUser.getId());

        final ResponseEntity<User> response = restTemplate.exchange(
            "http://localhost:" + port + "/users/" + anotherUser.getId(),
            HttpMethod.GET, HttpEntity.EMPTY, User.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        anotherUser.setEmail(null); // user privacy aspect
        anotherUser.setPhone(null); // user privacy aspect

        then(response.getBody()).isEqualTo(anotherUser);
    }

    @Test
    public void getByIdHimselfWhenNotAuth() {
        User user = createUser(TestUtils.getTestUser());

        final ResponseEntity<User> response = restTemplate.exchange(
            "http://localhost:" + port + "/users/" + user.getId(),
            HttpMethod.GET, HttpEntity.EMPTY, User.class);

        user.setEmail(null); // user privacy aspect
        user.setPhone(null); // user privacy aspect

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(user);
    }

    @Test
    public void getByIdHimself() {
        User user = createUser(TestUtils.getTestUser());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final ResponseEntity<User> response = restTemplate.exchange(
            "http://localhost:" + port + "/users/" + user.getId(),
            HttpMethod.GET, HttpEntity.EMPTY, User.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        then(response.getBody()).isEqualTo(user);
    }

    @Test
    public void getByIdWhenNotExist() {
        User user = createUser(TestUtils.getTestUser());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/users/" + 100500,
            HttpMethod.GET, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("User is not found");
    }

    @Test
    public void getByIds() {
        User user = createUser(TestUtils.getTestUser());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final RequestEntity<List<Integer>> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/users/multiple"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(List.of(user.getId()));

        final ResponseEntity<List<User>> response = restTemplate.exchange(
            request, new ParameterizedTypeReference<List<User>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        then(response.getBody()).isEqualTo(List.of(user));
    }

    @Test
    public void getByIdsWhenNotAuth() {
        User user = createUser(TestUtils.getTestUser());

        final RequestEntity<List<Integer>> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/users/multiple"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(List.of(user.getId()));

        final ResponseEntity<List<User>> response = restTemplate.exchange(
            request, new ParameterizedTypeReference<List<User>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        user.setEmail(null); // user privacy aspect
        user.setPhone(null); // user privacy aspect

        then(response.getBody()).isEqualTo(List.of(user));
    }

    @Test
    public void getByIdsWithAnotherUser() {
        User user = createUser(TestUtils.getTestUser());
        User anotherUser = createUser(prepareUser("test0", "test0@gmail.com", "88005553503"));

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final RequestEntity<List<Integer>> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/users/multiple"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(List.of(user.getId(), anotherUser.getId()));

        final ResponseEntity<List<User>> response = restTemplate.exchange(
            request, new ParameterizedTypeReference<List<User>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        anotherUser.setEmail(null); // user privacy aspect
        anotherUser.setPhone(null); // user privacy aspect

        then(response.getBody()).isEqualTo(List.of(user, anotherUser));
    }

    @Test
    public void getByIdsWithAnotherUserWhenNotAuth() {
        User user = createUser(TestUtils.getTestUser());
        User anotherUser = createUser(prepareUser("test0", "test0@gmail.com", "88005553503"));

        final RequestEntity<List<Integer>> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/users/multiple"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(List.of(user.getId(), anotherUser.getId()));

        final ResponseEntity<List<User>> response = restTemplate.exchange(
            request, new ParameterizedTypeReference<List<User>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        user.setEmail(null); // user privacy aspect
        user.setPhone(null); // user privacy aspect

        anotherUser.setEmail(null); // user privacy aspect
        anotherUser.setPhone(null); // user privacy aspect

        then(response.getBody()).isEqualTo(List.of(user, anotherUser));
    }

    @Test
    public void getByLogin() {
        User user = createUser(TestUtils.getTestUser());
        User anotherUser = createUser(prepareUser("test0", "test0@gmail.com", "88005553503"));

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final ResponseEntity<User> response = restTemplate.exchange(
            "http://localhost:" + port + "/users/login/" + user.getLogin(),
            HttpMethod.GET, HttpEntity.EMPTY, User.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(user);
    }

    @Test
    public void getByLoginWhenNotAuth() {
        User user = createUser(TestUtils.getTestUser());
        User anotherUser = createUser(prepareUser("test0", "test0@gmail.com", "88005553503"));

        final ResponseEntity<User> response = restTemplate.exchange(
            "http://localhost:" + port + "/users/login/" + user.getLogin(),
            HttpMethod.GET, HttpEntity.EMPTY, User.class);

        user.setEmail(null); // user privacy aspect
        user.setPhone(null); // user privacy aspect

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(user);
    }

    @Test
    public void getByLoginWhenNotExist() {
        User user = createUser(TestUtils.getTestUser());
        User anotherUser = createUser(prepareUser("test0", "test0@gmail.com", "88005553503"));

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/users/login/" + "qweqweqwe1qwe",
            HttpMethod.GET, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("User is not found");
    }

    @Test
    public void getUsersByCommunityNickname() {
        User firstUser = createUser(prepareUser("test0", "test0@gmail.com", "88005553503"));
        assertNotNull(firstUser.getId());

        User secondUser = createUser(prepareUser("test", "test@gmail.com", "88005553530"));
        assertNotNull(secondUser.getId());

        User thirdUser = createUser(prepareUser("test1", "test1@gmail.com", "88005553531"));
        assertNotNull(thirdUser.getId());

        when(accessService.getCurrentAuthUser()).thenReturn(secondUser);

        Community community = TestUtils.getTestCommunity();
        community.setUserIds(List.of(firstUser.getId(), thirdUser.getId()));
        community = daoFactory.getCommunityDao().create(community);

        final ResponseEntity<List<User>> response = restTemplate.exchange(
            "http://localhost:" + port + "/users/community/nickname/" + community.getNickname(),
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<User>>() {
            });

        firstUser.setEmail(null); // user privacy aspect
        firstUser.setPhone(null); // user privacy aspect

        thirdUser.setEmail(null); // user privacy aspect
        thirdUser.setPhone(null); // user privacy aspect

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(2);
        then(response.getBody().get(0)).isEqualTo(firstUser);
        then(response.getBody().get(1)).isEqualTo(thirdUser);
    }

    @Test
    public void getUsersByCommunityNicknameWhenOneIsCurUser() {
        User firstUser = createUser(prepareUser("test0", "test0@gmail.com", "88005553503"));
        assertNotNull(firstUser.getId());

        User secondUser = createUser(prepareUser("test", "test@gmail.com", "88005553530"));
        assertNotNull(secondUser.getId());

        User thirdUser = createUser(prepareUser("test1", "test1@gmail.com", "88005553531"));
        assertNotNull(thirdUser.getId());

        when(accessService.getCurrentAuthUser()).thenReturn(thirdUser);

        Community community = TestUtils.getTestCommunity();
        community.setUserIds(List.of(firstUser.getId(), thirdUser.getId()));
        community = daoFactory.getCommunityDao().create(community);

        final ResponseEntity<List<User>> response = restTemplate.exchange(
            "http://localhost:" + port + "/users/community/nickname/" + community.getNickname(),
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<User>>() {
            });

        firstUser.setEmail(null); // user privacy aspect
        firstUser.setPhone(null); // user privacy aspect

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(2);
        then(response.getBody().get(0)).isEqualTo(firstUser);
        then(response.getBody().get(1)).isEqualTo(thirdUser);
    }

    @Test
    public void getUsersByCommunityNicknameWhenNoOneInCommunity() {
        createUser(TestUtils.getTestUser());

        Community community = TestUtils.getTestCommunity();
        community.setUserIds(Collections.emptyList());
        community = daoFactory.getCommunityDao().create(community);

        final ResponseEntity<List<User>> response = restTemplate.exchange(
            "http://localhost:" + port + "/users/community/nickname/" + community.getNickname(),
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<User>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(0);
    }

    @Test
    public void getUsersByCommunityNicknameWhenNotExist() {
        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/users/community/nickname/" + 100500,
            HttpMethod.GET, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Community is not found");
    }

    @Test
    public void getAll() {
        User user = createUser(TestUtils.getTestUser());

        User firstUser = createUser(prepareUser("test0", "test0@gmail.com", "88005553503"));
        assertNotNull(firstUser.getId());

        User secondUser = createUser(prepareUser("test", "test@gmail.com", "88005553530"));
        assertNotNull(secondUser.getId());

        User thirdUser = createUser(prepareUser("test1", "test1@gmail.com", "88005553531"));
        assertNotNull(thirdUser.getId());
        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final ResponseEntity<List<User>> response = restTemplate.exchange(
            "http://localhost:" + port + "/users/all",
            HttpMethod.GET, HttpEntity.EMPTY,  new ParameterizedTypeReference<List<User>>() {
            });

        firstUser.setEmail(null); // user privacy aspect
        firstUser.setPhone(null); // user privacy aspect

        secondUser.setEmail(null); // user privacy aspect
        secondUser.setPhone(null); // user privacy aspect

        thirdUser.setEmail(null); // user privacy aspect
        thirdUser.setPhone(null); // user privacy aspect

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(List.of(user, firstUser, secondUser, thirdUser));
    }

    @Test
    public void getAllWhenNotAuth() {
        User user = createUser(TestUtils.getTestUser());

        User firstUser = createUser(prepareUser("test0", "test0@gmail.com", "88005553503"));
        assertNotNull(firstUser.getId());

        User secondUser = createUser(prepareUser("test", "test@gmail.com", "88005553530"));
        assertNotNull(secondUser.getId());

        User thirdUser = createUser(prepareUser("test1", "test1@gmail.com", "88005553531"));
        assertNotNull(thirdUser.getId());

        final ResponseEntity<List<User>> response = restTemplate.exchange(
            "http://localhost:" + port + "/users/all",
            HttpMethod.GET, HttpEntity.EMPTY,  new ParameterizedTypeReference<List<User>>() {
            });

        user.setEmail(null); // user privacy aspect
        user.setPhone(null); // user privacy aspect

        firstUser.setEmail(null); // user privacy aspect
        firstUser.setPhone(null); // user privacy aspect

        secondUser.setEmail(null); // user privacy aspect
        secondUser.setPhone(null); // user privacy aspect

        thirdUser.setEmail(null); // user privacy aspect
        thirdUser.setPhone(null); // user privacy aspect

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(List.of(user, firstUser, secondUser, thirdUser));
    }

    @Test
    public void update() {
        User user = createUser(TestUtils.getTestUser());
        when(accessService.getCurrentAuthUser()).thenReturn(user);

        user.setAbout("new");

        final RequestEntity<User> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/users/" + user.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(user);

        final ResponseEntity<User> response = restTemplate.exchange(
            request, User.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(user);
    }

    @Test
    public void updateWithDifferentId() {
        User user = createUser(TestUtils.getTestUser());
        when(accessService.getCurrentAuthUser()).thenReturn(user);

        user.setAbout("new");

        final RequestEntity<User> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/users/" + 100500))
            .contentType(MediaType.APPLICATION_JSON)
            .body(user);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo("Different identifiers in request path and body");
    }

    @Test
    public void updateWhenNotAuth() {
        User user = createUser(TestUtils.getTestUser());

        user.setAbout("new");

        final RequestEntity<User> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/users/" + user.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(user);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        then(response.getBody()).isEqualTo("Auth error");
    }

    @Test
    public void updateWhenNotOwner() {
        User user = createUser(TestUtils.getTestUser());
        when(accessService.getCurrentAuthUser()).thenReturn(user);

        User firstUser = createUser(prepareUser("test0", "test0@gmail.com", "88005553503"));

        firstUser.setAbout("new");

        final RequestEntity<User> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/users/" + firstUser.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(firstUser);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo("User can be updated by owner only");
    }

    private User prepareUser(final String login, String email, String phone) {
        User secondUser = TestUtils.getTestUser();
        secondUser.setLogin(login);
        secondUser.setEmail(email);
        secondUser.setPhone(phone);
        return secondUser;
    }
}
