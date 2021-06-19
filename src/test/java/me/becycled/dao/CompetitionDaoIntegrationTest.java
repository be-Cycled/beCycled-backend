package me.becycled.dao;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.community.Community;
import me.becycled.backend.model.entity.competition.Competition;
import me.becycled.backend.model.entity.route.Route;
import me.becycled.backend.model.entity.route.SportType;
import me.becycled.backend.model.entity.user.User;
import me.becycled.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author I1yi4
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompetitionDaoIntegrationTest extends BaseIntegrationTest {

    @BeforeEach
    public void setUp() {
        daoFactory.getUserDao().create(TestUtils.getTestUser());
        daoFactory.getRouteDao().create(TestUtils.getTestRoute());
        daoFactory.getCommunityDao().create(TestUtils.getTestCommunity());
    }

    @Test
    void create() {
        final Competition testCompetition = TestUtils.getTestCompetition();
        Competition competition = daoFactory.getCompetitionDao().create(testCompetition);
        assertNotNull(competition.getId());
        assertNotNull(competition.getCreatedAt());

        testCompetition.setId(competition.getId());
        testCompetition.setCreatedAt(competition.getCreatedAt());

        assertEquals(testCompetition.getId(), competition.getId());
        assertEquals(testCompetition.getOwnerUserId(), competition.getOwnerUserId());
        assertEquals(testCompetition.getCommunityId(), competition.getCommunityId());
        assertEquals(testCompetition.getPrivate(), competition.getPrivate());
        assertEquals(testCompetition.getStartDate(), competition.getStartDate());
        assertEquals(testCompetition.getRouteId(), competition.getRouteId());
        assertEquals(testCompetition.getSportTypes(), competition.getSportTypes());
        assertEquals(testCompetition.getUserIds(), competition.getUserIds());
        assertEquals(testCompetition.getVenue(), competition.getVenue());
        assertEquals(testCompetition.getDuration(), competition.getDuration());
        assertEquals(testCompetition.getDescription(), competition.getDescription());
        assertEquals(testCompetition.getCreatedAt(), competition.getCreatedAt());
    }

    @Test
    void getById() {
        final Competition competition = daoFactory.getCompetitionDao().create(TestUtils.getTestCompetition());
        final Competition createCompetition = daoFactory.getCompetitionDao().getById(competition.getId());

        assertEquals(createCompetition, competition);
    }

    @Test
    void getAll() {
        final Competition firstCompetition = daoFactory.getCompetitionDao().create(TestUtils.getTestCompetition());
        final Competition secondCompetition = daoFactory.getCompetitionDao().create(TestUtils.getTestCompetition());

        final List<Competition> all = daoFactory.getCompetitionDao().getAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(firstCompetition::equals));
        assertTrue(all.stream().anyMatch(secondCompetition::equals));
    }

    @Test
    void update() {
        User user = TestUtils.getTestUser();
        user.setLogin("1");
        user.setEmail("1@gmail.com");
        user.setPhone("89001600020");
        user = daoFactory.getUserDao().create(user);
        Community community = TestUtils.getTestCommunity();
        community.setNickname("testNickname");
        community = daoFactory.getCommunityDao().create(community);
        final Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());
        final Competition competition = daoFactory.getCompetitionDao().create(TestUtils.getTestCompetition());

        final Competition testCompetition = new Competition();
        testCompetition.setId(competition.getId());
        testCompetition.setOwnerUserId(user.getId());
        testCompetition.setCommunityId(community.getId());
        testCompetition.setPrivate(true);
        testCompetition.setStartDate(Instant.now());
        testCompetition.setRouteId(route.getId());
        testCompetition.setSportTypes(Arrays.asList(SportType.values()));
        testCompetition.setUserIds(List.of(1, 2));
        testCompetition.setVenue("BQ 128");
        testCompetition.setDuration(123);
        testCompetition.setDescription("test");
        testCompetition.setCreatedAt(Instant.now());

        final Competition dbCompetition = daoFactory.getCompetitionDao().update(testCompetition);
        testCompetition.setCreatedAt(dbCompetition.getCreatedAt());

        assertNotNull(dbCompetition.getId());
        assertEquals(testCompetition.getOwnerUserId(), dbCompetition.getOwnerUserId());
        assertEquals(testCompetition.getCommunityId(), dbCompetition.getCommunityId());
        assertEquals(testCompetition.getPrivate(), dbCompetition.getPrivate());
        assertEquals(competition.getRouteId(), dbCompetition.getRouteId());
        assertEquals(testCompetition.getSportTypes(), dbCompetition.getSportTypes());
        assertEquals(testCompetition.getUserIds(), dbCompetition.getUserIds());
        assertEquals(testCompetition.getVenue(), dbCompetition.getVenue());
        assertEquals(testCompetition.getDuration(), dbCompetition.getDuration());
        assertEquals(testCompetition.getDescription(), dbCompetition.getDescription());
        assertEquals(testCompetition.getCreatedAt(), dbCompetition.getCreatedAt());
    }
}
