/*
 * Copyright © 2018 ООО "Первая Мониторинговая Компания".
 * All rights reserved.
 */

package me.becycled.controller;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.community.Community;
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
public class CommunityControllerTest extends BaseIntegrationTest {

    @MockBean
    AccessService accessService;

    //region getById

    @Test
    public void getById() {
        createUser(TestUtils.getTestUser());

        Community community = TestUtils.getTestCommunity();
        community = daoFactory.getCommunityDao().create(community);

        final ResponseEntity<Community> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/" + community.getId(),
            HttpMethod.GET, HttpEntity.EMPTY, Community.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(community);
    }

    @Test
    public void getByIdWhenNotExist() {
        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/" + 100500,
            HttpMethod.GET, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Community is not found");
    }

    //endregion getById

    //region getByNickname

    @Test
    public void getByNickname() {
        createUser(TestUtils.getTestUser());

        Community community = TestUtils.getTestCommunity();
        community = daoFactory.getCommunityDao().create(community);

        final ResponseEntity<Community> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/nickname/" + community.getNickname(),
            HttpMethod.GET, HttpEntity.EMPTY, Community.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(community);
    }

    @Test
    public void getByNicknameWhenNotExist() {
        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/nickname/" + "100500",
            HttpMethod.GET, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Community is not found");
    }

    //endregion getByNickname

    //region getUsersByCommunityNickname

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
            "http://localhost:" + port + "/communities/nickname/" + community.getNickname() + "/users",
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<User>>() {
            });

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
            "http://localhost:" + port + "/communities/nickname/" + community.getNickname() + "/users",
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<User>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(0);
    }

    @Test
    public void getUsersByCommunityNicknameWhenNotExist() {
        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/nickname/" + "100500" + "/users",
            HttpMethod.GET, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Community is not found");
    }

    //endregion getUsersByCommunityNickname

    //region getCommunityWhichUserMemberByUserLogin

    @Test
    public void getCommunityWhichUserMemberByUserLogin() {
        User user = createUser(TestUtils.getTestUser());

        Community firstCommunity = TestUtils.getTestCommunity();
        firstCommunity.setNickname("first");
        firstCommunity.setUserIds(List.of(user.getId()));
        firstCommunity = daoFactory.getCommunityDao().create(firstCommunity);

        Community secondCommunity = TestUtils.getTestCommunity();
        secondCommunity.setNickname("second");
        secondCommunity.setUserIds(Collections.emptyList());
        secondCommunity = daoFactory.getCommunityDao().create(secondCommunity);

        Community thirdCommunity = TestUtils.getTestCommunity();
        thirdCommunity.setNickname("third");
        thirdCommunity.setUserIds(List.of(user.getId()));
        thirdCommunity = daoFactory.getCommunityDao().create(thirdCommunity);

        final ResponseEntity<List<Community>> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/user/" + user.getLogin(),
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Community>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(2);
        then(response.getBody().get(0)).isEqualTo(firstCommunity);
        then(response.getBody().get(1)).isEqualTo(thirdCommunity);
    }

    @Test
    public void getCommunityWhichUserMemberByUserLoginWhenNoOneCommunity() {
        User user = createUser(TestUtils.getTestUser());

        final ResponseEntity<List<Community>> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/user/" + user.getLogin(),
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Community>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(0);
    }

    @Test
    public void getCommunityWhichUserMemberByUserLoginWhenLoginNotExist() {
        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/user/" + "100500",
            HttpMethod.GET, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("User is not found");
    }

    //endregion getCommunityWhichUserMemberByUserLogin

    //region getAll

    @Test
    public void getAll() {
        createUser(TestUtils.getTestUser());

        Community firstCommunity = TestUtils.getTestCommunity();
        firstCommunity.setNickname("first");
        firstCommunity = daoFactory.getCommunityDao().create(firstCommunity);

        Community secondCommunity = TestUtils.getTestCommunity();
        secondCommunity.setNickname("second");
        secondCommunity = daoFactory.getCommunityDao().create(secondCommunity);

        Community thirdCommunity = TestUtils.getTestCommunity();
        thirdCommunity.setNickname("third");
        thirdCommunity = daoFactory.getCommunityDao().create(thirdCommunity);

        final ResponseEntity<List<Community>> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/all",
            HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Community>>() {
            });

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(3);
        then(response.getBody().get(0)).isEqualTo(firstCommunity);
        then(response.getBody().get(1)).isEqualTo(secondCommunity);
        then(response.getBody().get(2)).isEqualTo(thirdCommunity);
    }

    //endregion getAll

    //region create

    @Test
    public void create() {
        User user = TestUtils.getTestUser();
        user = createUser(user);

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Community community = TestUtils.getTestCommunity();
        community.setUserIds(Collections.emptyList());
        community.setOwnerUserId(100500);

        final RequestEntity<Community> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/communities"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(community);

        final ResponseEntity<Community> response = restTemplate.exchange(
            request, Community.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Community result = response.getBody();
        assertEquals(result.getOwnerUserId(), user.getId());

        community.setOwnerUserId(user.getId());
        community.setId(result.getId());
        community.setCreatedAt(result.getCreatedAt());
        community.setUserIds(Collections.singletonList(user.getId()));

        then(result).isEqualTo(community);
    }

    @Test
    public void createWhenOwnerInsideUserIds() {
        User user = TestUtils.getTestUser();
        user = createUser(user);

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Community community = TestUtils.getTestCommunity();
        community.setUserIds(Collections.singletonList(user.getId()));
        community.setOwnerUserId(100500);

        final RequestEntity<Community> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/communities"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(community);

        final ResponseEntity<Community> response = restTemplate.exchange(
            request, Community.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Community result = response.getBody();
        assertEquals(result.getOwnerUserId(), user.getId());

        community.setOwnerUserId(user.getId());
        community.setId(result.getId());
        community.setCreatedAt(result.getCreatedAt());
        community.setUserIds(Collections.singletonList(user.getId()));

        then(result).isEqualTo(community);
    }

    @Test
    public void createWhenNicknameAlreadyExist() {
        User user = TestUtils.getTestUser();
        user.setLogin("auth");
        user = createUser(user);

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Community community = TestUtils.getTestCommunity();
        community.setOwnerUserId(user.getId());
        daoFactory.getCommunityDao().create(community);

        final RequestEntity<Community> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/communities"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(community);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo("Community is already exist");
    }

    @Test
    public void createWhenNotAuth() {
        createUser(TestUtils.getTestUser());

        Community community = TestUtils.getTestCommunity();

        final RequestEntity<Community> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/communities"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(community);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        then(response.getBody()).isEqualTo("Auth error");
    }

    //endregion

    //region update

    @Test
    public void update() {
        User user = createUser(TestUtils.getTestUser());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Community community = TestUtils.getTestCommunity();
        community = daoFactory.getCommunityDao().create(community);

        community.setName("qwe");

        final RequestEntity<Community> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/communities/" + community.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(community);

        final ResponseEntity<Community> response = restTemplate.exchange(
            request, Community.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(community);
    }

    @Test
    public void updateWithDifferentId() {
        createUser(TestUtils.getTestUser());

        Community community = TestUtils.getTestCommunity();
        community = daoFactory.getCommunityDao().create(community);

        final RequestEntity<Community> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/communities/" + 100500))
            .contentType(MediaType.APPLICATION_JSON)
            .body(community);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo("Different identifiers in request path and body");
    }

    @Test
    public void updateWhenNotAuth() {
        createUser(TestUtils.getTestUser());

        Community community = TestUtils.getTestCommunity();
        community = daoFactory.getCommunityDao().create(community);

        final RequestEntity<Community> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/communities/" + community.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(community);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        then(response.getBody()).isEqualTo("Auth error");
    }

    @Test
    public void updateWhenNotExist() {
        User user = createUser(TestUtils.getTestUser());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Community community = TestUtils.getTestCommunity();
        community.setId(1);

        final RequestEntity<Community> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/communities/" + community.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(community);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Community is not found");
    }

    @Test
    public void updateWhenNotOwner() {
        User owner = createUser(TestUtils.getTestUser());
        User user = createUser(prepareUser("test", "test@gmail.com", "89990001122"));

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Community community = TestUtils.getTestCommunity();
        community.setOwnerUserId(owner.getId());
        community.setId(1);
        community = daoFactory.getCommunityDao().create(community);

        final RequestEntity<Community> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/communities/" + community.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(community);

        final ResponseEntity<String> response = restTemplate.exchange(
            request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo("Community can be updated by owner only");
    }

    //endregion update

    //region delete

    @Test
    public void delete() {
        User user = createUser(TestUtils.getTestUser());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Community community = TestUtils.getTestCommunity();
        community = daoFactory.getCommunityDao().create(community);

        final ResponseEntity<Integer> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/" + community.getId(),
            HttpMethod.DELETE, HttpEntity.EMPTY, Integer.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(1);
    }

    @Test
    public void deleteWhenNotAuth() {
        createUser(TestUtils.getTestUser());

        Community community = TestUtils.getTestCommunity();
        community = daoFactory.getCommunityDao().create(community);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/" + community.getId(),
            HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        then(response.getBody()).isEqualTo("Auth error");
    }

    @Test
    public void deleteWhenCommunityNotExist() {
        User user = createUser(TestUtils.getTestUser());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Community community = TestUtils.getTestCommunity();
        community = daoFactory.getCommunityDao().create(community);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/" + 100500,
            HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Community is not found");
    }

    @Test
    public void deleteWhenCommunityWhenNotOwner() {
        User owner = createUser(TestUtils.getTestUser());
        User user = createUser(prepareUser("test", "test@gmail.com", "89990001122"));

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Community community = TestUtils.getTestCommunity();
        community.setOwnerUserId(owner.getId());
        community = daoFactory.getCommunityDao().create(community);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/" + community.getId(),
            HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo("Community can be deleted by owner only");
    }

    //endregion delete

    //region join

    @Test
    public void join() {
        User user = createUser(TestUtils.getTestUser());

        User firstUser = createUser(prepareUser("test0", "test0@gmail.com", "88005553503"));
        assertNotNull(firstUser.getId());

        User secondUser = createUser(prepareUser("test", "test@gmail.com", "88005553530"));
        assertNotNull(secondUser.getId());

        User thirdUser = createUser(prepareUser("test1", "test1@gmail.com", "88005553531"));
        assertNotNull(thirdUser.getId());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Community community = TestUtils.getTestCommunity();
        community.setUserIds(List.of(firstUser.getId(), secondUser.getId(), thirdUser.getId()));
        community = daoFactory.getCommunityDao().create(community);

        final ResponseEntity<Community> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/join/" + community.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, Community.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        community.setUserIds(List.of(firstUser.getId(), secondUser.getId(), thirdUser.getId(), user.getId()));
        then(response.getBody()).isEqualTo(community);
    }

    @Test
    public void joinWhenNoOneInCommunity() {
        User user = createUser(TestUtils.getTestUser());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Community community = TestUtils.getTestCommunity();
        community.setUserIds(Collections.emptyList());
        community = daoFactory.getCommunityDao().create(community);

        final ResponseEntity<Community> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/join/" + community.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, Community.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        community.setUserIds(Collections.singletonList(user.getId()));

        then(response.getBody()).isEqualTo(community);
    }

    @Test
    public void joinWhenNotAuth() {
        createUser(TestUtils.getTestUser());

        Community community = TestUtils.getTestCommunity();
        community = daoFactory.getCommunityDao().create(community);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/join/" + community.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        then(response.getBody()).isEqualTo("Auth error");
    }

    @Test
    public void joinCommunityNotExist() {
        User user = createUser(TestUtils.getTestUser());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/join/" + 100500,
            HttpMethod.POST, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Community is not found");
    }

    @Test
    public void joinWhenAlreadyJoined() {
        User user = createUser(TestUtils.getTestUser());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Community community = TestUtils.getTestCommunity();
        community.setUserIds(Collections.singletonList(user.getId()));
        community = daoFactory.getCommunityDao().create(community);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/join/" + community.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo("Current user is already joined");
    }

    //endregion join

    //region leave

    @Test
    public void leave() {
        User user = createUser(TestUtils.getTestUser());

        User firstUser = createUser(prepareUser("test0", "test0@gmail.com", "88005553503"));
        assertNotNull(firstUser.getId());

        User secondUser = createUser(prepareUser("test", "test@gmail.com", "88005553530"));
        assertNotNull(secondUser.getId());

        User thirdUser = createUser(prepareUser("test1", "test1@gmail.com", "88005553531"));
        assertNotNull(thirdUser.getId());
        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Community community = TestUtils.getTestCommunity();
        community.setUserIds(List.of(firstUser.getId(), secondUser.getId(), thirdUser.getId(), user.getId()));

        community = daoFactory.getCommunityDao().create(community);

        final ResponseEntity<Community> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/leave/" + community.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, Community.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        community.setUserIds(List.of(firstUser.getId(), secondUser.getId(), thirdUser.getId()));
        then(response.getBody()).isEqualTo(community);
    }

    @Test
    public void leaveWhenNotAuth() {
        User user = createUser(TestUtils.getTestUser());

        Community community = TestUtils.getTestCommunity();
        community.setUserIds(List.of(user.getId()));

        community = daoFactory.getCommunityDao().create(community);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/leave/" + community.getId(),
            HttpMethod.POST, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        then(response.getBody()).isEqualTo("Auth error");
    }

    @Test
    public void leaveWhenCommunityNotExist() {
        User user = createUser(TestUtils.getTestUser());
        when(accessService.getCurrentAuthUser()).thenReturn(user);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/leave/" + 100500,
            HttpMethod.POST, HttpEntity.EMPTY, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo("Community is not found");
    }

    @Test
    public void leaveWhenNotJoined() {
        User user = createUser(TestUtils.getTestUser());

        User firstUser = createUser(prepareUser("test0", "test0@gmail.com", "88005553503"));
        assertNotNull(firstUser.getId());

        User secondUser = createUser(prepareUser("test", "test@gmail.com", "88005553530"));
        assertNotNull(secondUser.getId());

        User thirdUser = createUser(prepareUser("test1", "test1@gmail.com", "88005553531"));
        assertNotNull(thirdUser.getId());

        when(accessService.getCurrentAuthUser()).thenReturn(user);

        Community community = TestUtils.getTestCommunity();
        community.setUserIds(List.of(firstUser.getId(), secondUser.getId(), thirdUser.getId()));

        community = daoFactory.getCommunityDao().create(community);

        final ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/communities/leave/" + community.getId(),
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
