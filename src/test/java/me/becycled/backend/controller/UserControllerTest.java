package me.becycled.backend.controller;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.service.AccessService;
import me.becycled.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
    public void getByIdHimSelf() {
        User user = createUser(TestUtils.getTestUser());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final ResponseEntity<User> response = restTemplate.exchange(
            "http://localhost:" + port + "/users/" + user.getId(),
            HttpMethod.GET, HttpEntity.EMPTY, User.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        then(response.getBody()).isEqualTo(user);
    }

    @Test
    public void getById() {
        User curUser = createUser(TestUtils.getTestUser());
        when(accessService.getCurrentAuthUser()).thenReturn(curUser);

        User user = createUser(prepareUser("test0", "test0@gmail.com", "88005553503"));
        assertNotNull(user.getId());

        final ResponseEntity<User> response = restTemplate.exchange(
            "http://localhost:" + port + "/users/" + user.getId(),
            HttpMethod.GET, HttpEntity.EMPTY, User.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        user.setEmail(null); // user privacy aspect
        user.setPhone(null); // user privacy aspect

        then(response.getBody()).isEqualTo(user);
    }

    @Test
    public void getByIdWhenNotAuth() {
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
    public void getByIdWhenNotExist() {
        User user = createUser(TestUtils.getTestUser());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/users/" + 100500,
            HttpMethod.GET, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("User is not found");
    }

    private User prepareUser(final String login, String email, String phone) {
        User secondUser = TestUtils.getTestUser();
        secondUser.setLogin(login);
        secondUser.setEmail(email);
        secondUser.setPhone(phone);
        return secondUser;
    }
}
