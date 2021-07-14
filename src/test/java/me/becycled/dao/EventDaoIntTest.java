package me.becycled.dao;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.community.Community;
import me.becycled.backend.model.entity.event.EventType;
import me.becycled.backend.model.entity.route.Route;
import me.becycled.backend.model.entity.route.SportType;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.entity.user.UserAccount;
import me.becycled.backend.model.entity.event.Event;
import me.becycled.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Suren Kalaychyan
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventDaoIntTest extends BaseIntegrationTest {

    @BeforeEach
    void setUp() {
        daoFactory.getUserAccountDao().create(TestUtils.getTestUser(), TestUtils.getTestUserAccount());
        daoFactory.getRouteDao().create(TestUtils.getTestRoute());
    }

    @Test
    void create() {
        final Event testEvent = TestUtils.getTestEvent();
        Event event = daoFactory.getEventDao().create(testEvent);
        assertNotNull(event.getId());
        assertNotNull(event.getCreatedAt());

        testEvent.setId(event.getId());
        testEvent.setCreatedAt(event.getCreatedAt());

        assertEquals(testEvent.getId(), event.getId());
        assertEquals(testEvent.getOwnerUserId(), event.getOwnerUserId());
        assertEquals(testEvent.getCommunityId(), event.getCommunityId());
        assertEquals(testEvent.getPrivate(), event.getPrivate());
        assertEquals(testEvent.getStartDate(), event.getStartDate());
        assertEquals(testEvent.getRouteId(), event.getRouteId());
        assertEquals(testEvent.getSportType(), event.getSportType());
        assertEquals(testEvent.getUserIds(), event.getUserIds());
        assertEquals(testEvent.getVenueGeoData(), event.getVenueGeoData());
        assertEquals(testEvent.getDuration(), event.getDuration());
        assertEquals(testEvent.getDescription(), event.getDescription());
        assertEquals(testEvent.getCreatedAt(), event.getCreatedAt());
    }

    @Test
    void getByCommunityNickname() {
        daoFactory.getCommunityDao().create(TestUtils.getTestCommunity());

        final Event testEvent = TestUtils.getTestEvent();
        testEvent.setCommunityId(1);
        final Event event = daoFactory.getEventDao().create(testEvent);
        final String nickname = daoFactory.getCommunityDao().getById(testEvent.getId()).getNickname();
        final List<Event> bdEvent = daoFactory.getEventDao().getByCommunityNickname(nickname);

        assertEquals(1, bdEvent.size());
        assertEquals(event, bdEvent.get(0));
    }

    @Test
    void delete() {
        final Event event = daoFactory.getEventDao().create(TestUtils.getTestEvent());

        final Event createdEvent = daoFactory.getEventDao().getById(event.getId());
        assertEquals(createdEvent, event);

        int delete = daoFactory.getEventDao().delete(createdEvent.getId());
        assertEquals(1, delete);

        final Event afterDelete = daoFactory.getEventDao().getById(event.getId());
        assertNull(afterDelete);

        int deleteNotExist = daoFactory.getEventDao().delete(100500);
        assertEquals(0, deleteNotExist);
    }

    @Test
    void getById() {
        final Event event = daoFactory.getEventDao().create(TestUtils.getTestEvent());
        final Event createEvent = daoFactory.getEventDao().getById(event.getId());

        assertEquals(createEvent, event);
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

        Event eventFirst = TestUtils.getTestEvent();
        eventFirst.setUserIds(List.of(1, 2, 3));
        eventFirst = daoFactory.getEventDao().create(eventFirst);

        Event eventSecond = TestUtils.getTestEvent();
        eventSecond.setUserIds(List.of(3));
        eventSecond = daoFactory.getEventDao().create(eventSecond);

        Event eventThird = TestUtils.getTestEvent();
        eventThird.setUserIds(Collections.emptyList());
        eventThird = daoFactory.getEventDao().create(eventThird);

        final List<Event> result = daoFactory.getEventDao().getByMemberUserId(3);
        assertEquals(2, result.size());
        assertEquals(eventFirst, result.get(0));
        assertEquals(eventSecond, result.get(1));
    }

    @Test
    void getAffiche() {
        final User user = new User();
        user.setLogin("login1");
        user.setEmail("email1@gmail.com");
        daoFactory.getUserAccountDao().create(user, TestUtils.getTestUserAccount());
        user.setLogin("login2");
        user.setEmail("email2@gmail.com");
        daoFactory.getUserAccountDao().create(user, TestUtils.getTestUserAccount());

        final Instant now = Instant.now();

        Event eventFirst = TestUtils.getTestEvent();
        eventFirst.setUserIds(List.of(1, 2, 3));
        eventFirst.setStartDate(now.plus(1, ChronoUnit.HOURS));
        eventFirst.setDuration(1800);
        eventFirst = daoFactory.getEventDao().create(eventFirst);

        Event eventSecond = TestUtils.getTestEvent();
        eventSecond.setUserIds(List.of(3));
        eventSecond.setStartDate(now.minus(1, ChronoUnit.HOURS));
        eventSecond.setDuration(1800);
        eventSecond = daoFactory.getEventDao().create(eventSecond);

        Event eventThird = TestUtils.getTestEvent();
        eventThird.setUserIds(Collections.emptyList());
        eventThird.setStartDate(now.plus(240, ChronoUnit.HOURS));
        eventThird.setDuration(1800);
        eventThird = daoFactory.getEventDao().create(eventThird);

        final List<Event> result = daoFactory.getEventDao().getAffiche();
        assertEquals(2, result.size());
        assertEquals(eventFirst, result.get(0));
        assertEquals(eventThird, result.get(1));
    }

    @Test
    void getFeed() {
        final User user = new User();
        user.setLogin("login1");
        user.setEmail("email1@gmail.com");
        daoFactory.getUserAccountDao().create(user, TestUtils.getTestUserAccount());
        user.setLogin("login2");
        user.setEmail("email2@gmail.com");
        daoFactory.getUserAccountDao().create(user, TestUtils.getTestUserAccount());

        final Instant now = Instant.now();

        Event eventFirst = TestUtils.getTestEvent();
        eventFirst.setUserIds(List.of(1, 2, 3));
        eventFirst.setStartDate(now.plus(1, ChronoUnit.HOURS));
        eventFirst.setDuration(1800);
        eventFirst = daoFactory.getEventDao().create(eventFirst);

        Event eventSecond = TestUtils.getTestEvent();
        eventSecond.setUserIds(List.of(3));
        eventSecond.setStartDate(now.minus(1, ChronoUnit.HOURS));
        eventSecond.setDuration(1800);
        eventSecond = daoFactory.getEventDao().create(eventSecond);

        Event eventThird = TestUtils.getTestEvent();
        eventThird.setUserIds(Collections.emptyList());
        eventThird.setStartDate(now.plus(240, ChronoUnit.HOURS));
        eventThird.setDuration(1800);
        eventThird = daoFactory.getEventDao().create(eventThird);

        final List<Event> result = daoFactory.getEventDao().getFeed();
        assertEquals(1, result.size());
        assertEquals(eventSecond, result.get(0));
    }

    @Test
    void getAll() {
        final Event firstEvent = daoFactory.getEventDao().create(TestUtils.getTestEvent());
        final Event secondEvent = daoFactory.getEventDao().create(TestUtils.getTestEvent());

        final List<Event> all = daoFactory.getEventDao().getAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(firstEvent::equals));
        assertTrue(all.stream().anyMatch(secondEvent::equals));
    }

    @Test
    void update() {
        User user = TestUtils.getTestUser();
        user.setLogin("1");
        user.setEmail("1@gmail.com");
        user.setPhone("89001600020");
        UserAccount userAccount = daoFactory.getUserAccountDao().create(user, TestUtils.getTestUserAccount());
        user = daoFactory.getUserDao().getById(userAccount.getUserId());
        Community community = TestUtils.getTestCommunity();
        community.setNickname("testNickname");
        community = daoFactory.getCommunityDao().create(community);
        final Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());
        final Event event = daoFactory.getEventDao().create(TestUtils.getTestEvent());

        final Event testEvent = new Event();
        testEvent.setId(event.getId());
        testEvent.setOwnerUserId(user.getId());
        testEvent.setCommunityId(community.getId());
        testEvent.setEventType(EventType.COMPETITION);
        testEvent.setPrivate(true);
        testEvent.setStartDate(Instant.now());
        testEvent.setRouteId(route.getId());
        testEvent.setSportType(SportType.BICYCLE);
        testEvent.setUserIds(List.of(1, 2));
        testEvent.setVenueGeoData("BQ 128");
        testEvent.setDuration(123);
        testEvent.setDescription("test");
        testEvent.setCreatedAt(Instant.now());

        final Event dbEvent = daoFactory.getEventDao().update(testEvent);
        testEvent.setCreatedAt(dbEvent.getCreatedAt());

        assertNotNull(dbEvent.getId());
        assertEquals(event.getOwnerUserId(), dbEvent.getOwnerUserId());
        assertEquals(testEvent.getCommunityId(), dbEvent.getCommunityId());
        assertEquals(testEvent.getPrivate(), dbEvent.getPrivate());
        assertEquals(event.getRouteId(), dbEvent.getRouteId());
        assertEquals(testEvent.getSportType(), dbEvent.getSportType());
        assertEquals(testEvent.getUserIds(), dbEvent.getUserIds());
        assertEquals(testEvent.getVenueGeoData(), dbEvent.getVenueGeoData());
        assertEquals(testEvent.getDuration(), dbEvent.getDuration());
        assertEquals(testEvent.getDescription(), dbEvent.getDescription());
        assertEquals(event.getCreatedAt(), dbEvent.getCreatedAt());
    }
}
