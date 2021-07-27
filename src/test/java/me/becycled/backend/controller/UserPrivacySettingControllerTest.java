package me.becycled.backend.controller;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.community.Community;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.entity.userprivacysetting.UserPrivacySetting;
import me.becycled.backend.service.AccessService;
import me.becycled.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.when;

/**
 * @author I1yi4
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserPrivacySettingControllerTest extends BaseIntegrationTest {

    @MockBean
    AccessService accessService;

    @Test
    public void getByUserId() {
        User user = createUser(TestUtils.getTestUser());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        UserPrivacySetting privacySetting = TestUtils.getUserPrivacySetting();
        privacySetting = daoFactory.getUserPrivacySettingDao().create(privacySetting);

        final ResponseEntity<UserPrivacySetting> response = restTemplate.exchange(
            "http://localhost:" + port + "/user-privacy-settings/" + privacySetting.getUserId(),
            HttpMethod.GET, HttpEntity.EMPTY, UserPrivacySetting.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(privacySetting);
    }

    @Test
    public void create() {
        User user = TestUtils.getTestUser();
        user = createUser(user);

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        UserPrivacySetting privacySetting = TestUtils.getUserPrivacySetting();

        final RequestEntity<UserPrivacySetting> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/user-privacy-settings"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(privacySetting);

        final ResponseEntity<UserPrivacySetting> response = restTemplate.exchange(
            request, UserPrivacySetting.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(privacySetting);
    }
}
