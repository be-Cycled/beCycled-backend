package me.becycled.backend.model.dao;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.route.RoutePhoto;
import me.becycled.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
public class RoutePhotoDaoIntTest extends BaseIntegrationTest {

    @Test
    void create() {
        createUser(TestUtils.getTestUser());
        daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        final RoutePhoto routePhoto = new RoutePhoto();
        routePhoto.setRouteId(1);
        routePhoto.setPhoto("avatar");

        final RoutePhoto photo = daoFactory.getRoutePhotoDao().create(routePhoto);
        assertNotNull(photo.getId());
        assertNotNull(photo.getCreatedAt());

        routePhoto.setId(photo.getId());
        routePhoto.setCreatedAt(photo.getCreatedAt());

        assertEquals(photo, routePhoto);
    }

    @Test
    void getById() {
        createUser(TestUtils.getTestUser());
        daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        final RoutePhoto routePhoto = daoFactory.getRoutePhotoDao().create(TestUtils.getTestRoutePhoto());
        final RoutePhoto createdRoutePhoto = daoFactory.getRoutePhotoDao().getById(routePhoto.getId());

        assertEquals(createdRoutePhoto, routePhoto);
    }

    @Test
    void getByRouteId() {
        createUser(TestUtils.getTestUser());
        daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        final RoutePhoto routePhoto = daoFactory.getRoutePhotoDao().create(TestUtils.getTestRoutePhoto());
        List<RoutePhoto> routePhotos = daoFactory.getRoutePhotoDao().getByRouteId(routePhoto.getRouteId());

        assertEquals(routePhoto, routePhotos.get(0));
    }

    @Test
    void getAll() {
        createUser(TestUtils.getTestUser());
        daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        RoutePhoto routePhotoFirst = daoFactory.getRoutePhotoDao().create(TestUtils.getTestRoutePhoto());
        RoutePhoto routePhotoSecond = daoFactory.getRoutePhotoDao().create(TestUtils.getTestRoutePhoto());

        final List<RoutePhoto> all = daoFactory.getRoutePhotoDao().getAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(routePhotoFirst::equals));
        assertTrue(all.stream().anyMatch(routePhotoSecond::equals));
    }

    @Test
    void delete() {
        createUser(TestUtils.getTestUser());
        daoFactory.getRouteDao().create(TestUtils.getTestRoute());

        final RoutePhoto routePhoto = daoFactory.getRoutePhotoDao().create(TestUtils.getTestRoutePhoto());

        final int delete = daoFactory.getRoutePhotoDao().delete(routePhoto.getId());
        assertEquals(1, delete);

        final int anotherDelete = daoFactory.getRoutePhotoDao().delete(routePhoto.getId());
        assertEquals(0, anotherDelete);
    }
}
