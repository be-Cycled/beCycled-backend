package me.becycled.dao;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.community.Community;
import me.becycled.backend.model.entity.community.CommunityType;
import me.becycled.backend.model.entity.route.SportType;
import me.becycled.backend.model.entity.user.User;
import me.becycled.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author I1yi4
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommunityDaoIntegrationTest extends BaseIntegrationTest {

    @Test
    void create() {
        final User testUser = TestUtils.getTestUser();
        daoFactory.getUserDao().create(testUser);

        final Community community = new Community();
        community.setOwnerUserId(1);
        community.setName("name");
        community.setNickname("nickname");
        community.setAvatar("avatar");
        community.setCommunityType(CommunityType.ORGANIZATION);
        community.setSportTypes(List.of(SportType.BICYCLE, SportType.RUN));
        community.setUserIds(List.of(1));
        community.setUrl("url");
        community.setDescription("description");
        final Community dbCommunity = daoFactory.getCommunityDao().create(community);
        assertNotNull(dbCommunity.getId());
        assertNotNull(dbCommunity.getCreatedAt());

        community.setId(dbCommunity.getId());
        community.setCreatedAt(dbCommunity.getCreatedAt());

        assertEquals(community, dbCommunity);
    }

    @Test
    void update() {
        final User testUser = TestUtils.getTestUser();
        daoFactory.getUserDao().create(testUser);
        final User user = TestUtils.getTestUser();
        user.setLogin("1");
        user.setEmail("1@gmail.com");
        user.setPhone("89001600020");
        daoFactory.getUserDao().create(user);

        final Community community = daoFactory.getCommunityDao().create(TestUtils.getTestCommunity());

        final Community testCommunity = new Community();
        testCommunity.setId(community.getId());
        testCommunity.setOwnerUserId(2);
        testCommunity.setName("1");
        testCommunity.setNickname("2");
        testCommunity.setAvatar("avatar");
        testCommunity.setCommunityType(CommunityType.CLUB);
        testCommunity.setSportTypes(List.of(SportType.BICYCLE));
        testCommunity.setUserIds(List.of(1, 2));
        testCommunity.setUrl("1000");
        testCommunity.setDescription("1");
        testCommunity.setCreatedAt(Instant.now());

        final Community bdCommunity = daoFactory.getCommunityDao().update(testCommunity);
        assertNotNull(bdCommunity.getId());
        assertEquals(1, bdCommunity.getOwnerUserId().intValue());
        assertEquals("1", bdCommunity.getName());
        assertEquals("2", bdCommunity.getNickname());
        assertEquals("avatar", bdCommunity.getAvatar());
        assertEquals(CommunityType.CLUB, bdCommunity.getCommunityType());
        assertEquals(List.of(SportType.BICYCLE), bdCommunity.getSportTypes());
        assertEquals(List.of(1, 2), bdCommunity.getUserIds());
        assertEquals("1000", bdCommunity.getUrl());
        assertEquals("1", bdCommunity.getDescription());
        assertEquals(community.getCreatedAt(), bdCommunity.getCreatedAt());
    }

    @Test
    void getById() {
        daoFactory.getUserDao().create(TestUtils.getTestUser());

        final Community community = daoFactory.getCommunityDao().create(TestUtils.getTestCommunity());
        final Community createdCommunity = daoFactory.getCommunityDao().getById(community.getId());

        assertEquals(createdCommunity, community);
    }

    @Test
    void getByNickname() {
        daoFactory.getUserDao().create(TestUtils.getTestUser());

        final Community community = daoFactory.getCommunityDao().create(TestUtils.getTestCommunity());
        final Community createdCommunity = daoFactory.getCommunityDao().getByNickname(community.getNickname());

        assertEquals(createdCommunity, community);
    }

    @Test
    void getByUserOwnerId() {
        daoFactory.getUserDao().create(TestUtils.getTestUser());
        final User user = new User();
        user.setLogin("login1");
        user.setEmail("email1@gmail.com");
        daoFactory.getUserDao().create(user);

        Community testCommunity = TestUtils.getTestCommunity();
        final Community communityFirst = daoFactory.getCommunityDao().create(testCommunity);
        testCommunity.setNickname("qwe");
        Community communitySecond = daoFactory.getCommunityDao().create(testCommunity);
        testCommunity.setNickname("rty");
        testCommunity.setOwnerUserId(2);
        Community communityThird = daoFactory.getCommunityDao().create(testCommunity);

        final List<Community> byOwedUserId = daoFactory.getCommunityDao().getByOwnerUserId(1);
        assertEquals(2, byOwedUserId.size());
        assertTrue(byOwedUserId.stream().anyMatch(communityFirst::equals));
        assertTrue(byOwedUserId.stream().anyMatch(communitySecond::equals));
    }

    @Test
    void getByMemberUserId() {
        daoFactory.getUserDao().create(TestUtils.getTestUser());
        final User user = new User();
        user.setLogin("login1");
        user.setEmail("email1@gmail.com");
        daoFactory.getUserDao().create(user);

        Community testCommunity = TestUtils.getTestCommunity();
        final Community communityFirst = daoFactory.getCommunityDao().create(testCommunity);
        testCommunity.setNickname("qwe");
        testCommunity.setUserIds(List.of(1, 2, 3));
        Community communitySecond = daoFactory.getCommunityDao().create(testCommunity);
        testCommunity.setNickname("rty");
        testCommunity.setOwnerUserId(2);
        testCommunity.setUserIds(List.of(3));
        Community communityThird = daoFactory.getCommunityDao().create(testCommunity);

        final List<Community> byOwedUserId = daoFactory.getCommunityDao().getByMemberUserId(3);
        assertEquals(2, byOwedUserId.size());
        assertEquals(2, byOwedUserId.stream().filter(community -> community.getUserIds().contains(3)).count());
    }

    @Test
    void getAll() {
        daoFactory.getUserDao().create(TestUtils.getTestUser());

        Community testCommunity = TestUtils.getTestCommunity();
        final Community communityFirst = daoFactory.getCommunityDao().create(testCommunity);
        testCommunity.setNickname("qwe");
        Community communitySecond = daoFactory.getCommunityDao().create(testCommunity);

        final List<Community> all = daoFactory.getCommunityDao().getAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(communityFirst::equals));
        assertTrue(all.stream().anyMatch(communitySecond::equals));
    }
}
