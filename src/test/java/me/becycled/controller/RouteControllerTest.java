/*
 * Copyright © 2018 ООО "Первая Мониторинговая Компания".
 * All rights reserved.
 */

package me.becycled.controller;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.route.Route;
import me.becycled.backend.model.entity.user.User;
import me.becycled.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author I1yi4
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RouteControllerTest extends BaseIntegrationTest {

    @Test
    public void getById() {
        User user = TestUtils.getTestUser();
        user = daoFactory.getUserDao().create(user);

        Route route = TestUtils.getTestRoute();
        route = daoFactory.getRouteDao().create(route);

        final ResponseEntity<Route> response = restTemplate.exchange(
            "http://localhost:" + port + "/routes/" + route.getId(),
            HttpMethod.GET, HttpEntity.EMPTY, Route.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(route);
    }

    @Test
    public void create() {
        User user = TestUtils.getTestUser();
        user = daoFactory.getUserDao().create(user);

        Route route = TestUtils.getTestRoute();

        final RequestEntity<Route> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/routes"))
            .body(route);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
