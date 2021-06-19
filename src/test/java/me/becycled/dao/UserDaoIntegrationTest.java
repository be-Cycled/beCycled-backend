package me.becycled.dao;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.user.User;
import me.becycled.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author I1yi4
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserDaoIntegrationTest extends BaseIntegrationTest {

    @Test
    void create() {
        final User user = new User();
        user.setLogin("zxc");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("eamail@gmail.com");
        user.setPhone("88005583535");
        user.setAbout("about");
        user.setAvatar("avatar");

        final User qwe = daoFactory.getUserDao().create(TestUtils.getTestUser());
        final User createdUser = daoFactory.getUserDao().create(user);
        assertNotNull(createdUser.getId());
        assertEquals(user.getLogin(), createdUser.getLogin());
        assertEquals(user.getFirstName(), createdUser.getFirstName());
        assertEquals(user.getLastName(), createdUser.getLastName());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getPhone(), createdUser.getPhone());
        assertEquals(user.getAbout(), createdUser.getAbout());
        assertEquals(user.getAvatar(), createdUser.getAvatar());
        assertNotNull(createdUser.getCreatedAt());
    }

    @Test
    void getById() {
        final User createdUser = daoFactory.getUserDao().create(TestUtils.getTestUser());
        assertNotNull(createdUser.getId());

        final User bdUser = daoFactory.getUserDao().getById(createdUser.getId());
        assertEquals(createdUser, bdUser);
    }

    @Test
    void getByLogin() {
        final User createdUser = daoFactory.getUserDao().create(TestUtils.getTestUser());
        assertNotNull(createdUser.getId());

        final User bdUser = daoFactory.getUserDao().getByLogin(createdUser.getLogin());
        assertEquals(createdUser, bdUser);
    }

    @Test
    void getByEmail() {
        final User createdUser = daoFactory.getUserDao().create(TestUtils.getTestUser());
        assertNotNull(createdUser.getId());

        final User bdUser = daoFactory.getUserDao().getByEmail(createdUser.getEmail());
        assertEquals(createdUser, bdUser);
    }

    @Test
    void getAll() {
        final User user = new User();
        user.setLogin("login1");
        user.setEmail("email1@gmail.com");

        final User createdUserFirst = daoFactory.getUserDao().create(user);
        final User createdUserSecond = daoFactory.getUserDao().create(TestUtils.getTestUser());

        final List<User> all = daoFactory.getUserDao().getAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(createdUserFirst::equals));
        assertTrue(all.stream().anyMatch(createdUserSecond::equals));
    }

    @Test
    void update() {
        final User user = new User();
        user.setLogin("q");
        user.setEmail("w");

        final User createdUser = daoFactory.getUserDao().create(user);

        user.setLogin("login");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@gmail.com");
        user.setPhone("88005553535");
        user.setAbout("about");
        user.setAvatar("avatar");

        daoFactory.getUserDao().update(user);

        final User bdUser = daoFactory.getUserDao().getById(createdUser.getId());
        assertNotNull(createdUser.getId());
        assertEquals("login", bdUser.getLogin());
        assertEquals("firstName", bdUser.getFirstName());
        assertEquals("lastName", bdUser.getLastName());
        assertEquals("email@gmail.com", bdUser.getEmail());
        assertEquals("88005553535", bdUser.getPhone());
        assertEquals("about", bdUser.getAbout());
        assertEquals("avatar", bdUser.getAvatar());
        assertEquals(createdUser.getCreatedAt(), bdUser.getCreatedAt());
    }
}
