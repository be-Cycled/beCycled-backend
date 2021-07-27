/*
 * Copyright © 2018 ООО "Первая Мониторинговая Компания".
 * All rights reserved.
 */

package me.becycled.backend.controller;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.dto.UserRegistrationDto;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.entity.user.UserAccount;
import me.becycled.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author I1yi4
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterControllerTest extends BaseIntegrationTest {

    @Test
    public void create() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setLogin("login");
        registrationDto.setEmail("email");
        registrationDto.setPassword("password");

        final RequestEntity<UserRegistrationDto> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/register"))
            .body(registrationDto);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        User createdUser = daoFactory.getUserDao().getByLogin(registrationDto.getLogin());
        UserAccount createdUserAccount = daoFactory.getUserAccountDao().getByUserId(createdUser.getId());

        assertNotNull(createdUser);
        assertNotNull(createdUserAccount);
    }

    @Test
    public void createWhenLoginAlreadyExist() {
        User testUser = TestUtils.getTestUser();
        testUser.setLogin("login");
        createUser(testUser);

        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setLogin("login");
        registrationDto.setEmail("email");
        registrationDto.setPassword("password");

        final RequestEntity<UserRegistrationDto> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/register"))
            .body(registrationDto);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo("Login is already using");
    }

    @Test
    public void createWhenEmailAlreadyExist() {
        User testUser = TestUtils.getTestUser();
        testUser.setLogin("qwe");
        testUser.setEmail("email");
        createUser(testUser);

        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setLogin("login");
        registrationDto.setEmail("email");
        registrationDto.setPassword("password");

        final RequestEntity<UserRegistrationDto> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/register"))
            .body(registrationDto);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo("Email is already using");
    }
}
