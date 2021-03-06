package me.becycled.backend.model.dao;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.route.Route;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author I1yi4
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RouteDaoIntTest extends BaseIntegrationTest {

    @Test
    void create() {
        final User testUser = TestUtils.getTestUser();
        createUser(testUser);

        final Route testRoute = new Route();
        testRoute.setUserId(1);
        testRoute.setName("name");
        testRoute.setRouteGeoData(TestUtils.getTestRoute().getRouteGeoData());
        testRoute.setRoutePreview("new preview");
        testRoute.setSportTypes(List.of(SportType.BICYCLE, SportType.RUN));
        testRoute.setDisposable(false);
        testRoute.setDescription("description");
        testRoute.setPopularity(100);
        final Route route = daoFactory.getRouteDao().create(testRoute);
        assertNotNull(route.getId());
        assertNotNull(route.getCreatedAt());

        testRoute.setId(route.getId());
        testRoute.setCreatedAt(route.getCreatedAt());

        assertEquals(testRoute.getId(), route.getId());
        assertEquals(testRoute.getName(), route.getName());
        assertEquals(testRoute.getRouteGeoData(), route.getRouteGeoData());
        assertEquals(testRoute.getRoutePreview(), route.getRoutePreview());
        assertEquals(testRoute.getDescription(), route.getDescription());
        assertEquals(testRoute.getDisposable(), route.getDisposable());
        assertEquals(0, route.getPopularity().intValue());
        assertEquals(testRoute.getCreatedAt(), route.getCreatedAt());
    }

    @Test
    void getById() {
        final User testUser = TestUtils.getTestUser();
        createUser(testUser);

        final Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());
        final Route createdRoute = daoFactory.getRouteDao().getById(route.getId());

        assertEquals(createdRoute, route);
    }

    @Test
    void getAll() {
        final User testUser = TestUtils.getTestUser();
        createUser(testUser);

        final Route routeFirst = daoFactory.getRouteDao().create(TestUtils.getTestRoute());
        final Route routeSecond = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        final List<Route> all = daoFactory.getRouteDao().getAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(routeFirst::equals));
        assertTrue(all.stream().anyMatch(routeSecond::equals));
    }

    @Test
    void update() {
        final User testUser = TestUtils.getTestUser();
        createUser(testUser);
        final User user = TestUtils.getTestUser();
        user.setLogin("1");
        user.setEmail("1@gmail.com");
        user.setPhone("89001600020");
        createUser(user);

        final Route route = daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        final Route testRoute = new Route();
        testRoute.setId(route.getId());
        testRoute.setUserId(2);
        testRoute.setName("qwe");
        testRoute.setRouteGeoData("test");
        testRoute.setRoutePreview("I1yi4");
        testRoute.setSportTypes(List.of(SportType.BICYCLE));
        testRoute.setDisposable(true);
        testRoute.setDescription("1");
        testRoute.setPopularity(5);
        testRoute.setCreatedAt(Instant.now());

        final Route bdRoute = daoFactory.getRouteDao().update(testRoute);
        assertNotNull(bdRoute.getId());
        assertEquals(1, bdRoute.getUserId().intValue());
        assertEquals("qwe", bdRoute.getName());
        assertEquals("test", bdRoute.getRouteGeoData());
        assertEquals("I1yi4", bdRoute.getRoutePreview());
        assertEquals(List.of(SportType.BICYCLE), bdRoute.getSportTypes());
        assertEquals(true, bdRoute.getDisposable());
        assertEquals(route.getPopularity(), bdRoute.getPopularity());
        assertEquals(route.getCreatedAt(), bdRoute.getCreatedAt());
    }
}
