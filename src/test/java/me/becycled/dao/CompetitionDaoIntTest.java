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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author I1yi4
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompetitionDaoIntTest extends BaseIntegrationTest {

    @BeforeEach
    public void setUp() {
        createUser(TestUtils.getTestUser());
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
        assertEquals(testCompetition.getSportType(), competition.getSportType());
        assertEquals(testCompetition.getUserIds(), competition.getUserIds());
        assertEquals(testCompetition.getVenueGeoData(), competition.getVenueGeoData());
        assertEquals(testCompetition.getDuration(), competition.getDuration());
        assertEquals(testCompetition.getDescription(), competition.getDescription());
        assertEquals(testCompetition.getCreatedAt(), competition.getCreatedAt());
    }

    @Test
    void update() {
        User user = TestUtils.getTestUser();
        user.setLogin("1");
        user.setEmail("1@gmail.com");
        user.setPhone("89001600020");
        user = createUser(user);
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
        testCompetition.setSportType(SportType.BICYCLE);
        testCompetition.setUserIds(List.of(1, 2));
        testCompetition.setVenueGeoData("BQ 128");
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
        assertEquals(testCompetition.getSportType(), dbCompetition.getSportType());
        assertEquals(testCompetition.getUserIds(), dbCompetition.getUserIds());
        assertEquals(testCompetition.getVenueGeoData(), dbCompetition.getVenueGeoData());
        assertEquals(testCompetition.getDuration(), dbCompetition.getDuration());
        assertEquals(testCompetition.getDescription(), dbCompetition.getDescription());
        assertEquals(testCompetition.getCreatedAt(), dbCompetition.getCreatedAt());
    }

    @Test
    void delete() {
        final Competition competition = daoFactory.getCompetitionDao().create(TestUtils.getTestCompetition());

        final Competition createdCompetition = daoFactory.getCompetitionDao().getById(competition.getId());
        assertEquals(createdCompetition, competition);

        int delete = daoFactory.getCompetitionDao().delete(createdCompetition.getId());
        assertEquals(1, delete);

        final Competition afterDelete = daoFactory.getCompetitionDao().getById(competition.getId());
        assertNull(afterDelete);

        int deleteNotExist = daoFactory.getCompetitionDao().delete(100500);
        assertEquals(0, deleteNotExist);
    }

    @Test
    void getById() {
        final Competition competition = daoFactory.getCompetitionDao().create(TestUtils.getTestCompetition());
        final Competition createCompetition = daoFactory.getCompetitionDao().getById(competition.getId());

        assertEquals(createCompetition, competition);
    }

    @Test
    void getByCommunityNickname() {
        final Competition testCompetition = TestUtils.getTestCompetition();
        testCompetition.setCommunityId(1);
        final Competition competition = daoFactory.getCompetitionDao().create(testCompetition);
        final String nickname = daoFactory.getCommunityDao().getById(testCompetition.getId()).getNickname();
        final List<Competition> bdCompetition = daoFactory.getCompetitionDao().getByCommunityNickname(nickname);

        assertEquals(1, bdCompetition.size());
        assertEquals(competition, bdCompetition.get(0));
    }

    @Test
    void getByMemberUserId() {
        final User user = new User();
        user.setLogin("login1");
        user.setEmail("email1@gmail.com");
        daoFactory.getUserAccountDao().create(user, TestUtils.getTestUserAccount());
        user.setLogin("login2");
        user.setEmail("email2@gmail.com");
        daoFactory.getUserAccountDao().create(user, TestUtils.getTestUserAccount());

        Competition competitionFirst = TestUtils.getTestCompetition();
        competitionFirst.setUserIds(List.of(1, 2, 3));
        competitionFirst = daoFactory.getCompetitionDao().create(competitionFirst);

        Competition competitionSecond = TestUtils.getTestCompetition();
        competitionSecond.setUserIds(List.of(3));
        competitionSecond = daoFactory.getCompetitionDao().create(competitionSecond);

        Competition competitionThird = TestUtils.getTestCompetition();
        competitionThird.setUserIds(Collections.emptyList());
        competitionThird = daoFactory.getCompetitionDao().create(competitionThird);

        final List<Competition> result = daoFactory.getCompetitionDao().getByMemberUserId(3);
        assertEquals(2, result.size());
        assertEquals(competitionFirst, result.get(0));
        assertEquals(competitionSecond, result.get(1));
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
}
