package me.becycled.dao;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.entity.user.UserAccount;
import me.becycled.utils.TestUtils;
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

/**
 * @author I1yi4
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserDaoIntTest extends BaseIntegrationTest {

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

        UserAccount testUserAccount = daoFactory.getUserAccountDao().create(TestUtils.getTestUser(), TestUtils.getTestUserAccount());
        final User createdUser = createUser(user);
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
        UserAccount userAccount = daoFactory.getUserAccountDao().create(TestUtils.getTestUser(), TestUtils.getTestUserAccount());
        User createdUser = daoFactory.getUserDao().getAll().get(0);
        assertNotNull(createdUser.getId());

        final User bdUser = daoFactory.getUserDao().getById(userAccount.getUserId());
        assertEquals(createdUser, bdUser);
    }

    @Test
    void getByIds() {
        User firstUser = TestUtils.getTestUser();
        UserAccount firstUserAccount = daoFactory.getUserAccountDao().create(firstUser, TestUtils.getTestUserAccount());
        firstUser = daoFactory.getUserDao().getById(firstUserAccount.getUserId());
        assertNotNull(firstUser.getId());

        User secondUser = TestUtils.getTestUser();
        secondUser.setLogin("test");
        secondUser.setEmail("test@gmail.com");
        secondUser.setPhone("88005553530");
        UserAccount secondUserAccount = daoFactory.getUserAccountDao().create(secondUser, TestUtils.getTestUserAccount());
        secondUser = daoFactory.getUserDao().getById(secondUserAccount.getUserId());
        assertNotNull(secondUser.getId());

        User thirdUser = TestUtils.getTestUser();
        thirdUser.setLogin("test1");
        thirdUser.setEmail("test1@gmail.com");
        thirdUser.setPhone("88005553531");
        UserAccount thirdUserAccount = daoFactory.getUserAccountDao().create(thirdUser, TestUtils.getTestUserAccount());
        thirdUser = daoFactory.getUserDao().getById(thirdUserAccount.getUserId());
        assertNotNull(thirdUser.getId());

        final List<User> result = daoFactory.getUserDao().getByIds(List.of(firstUser.getId(), thirdUser.getId(), 100500));
        assertEquals(2, result.size());
        assertEquals(firstUser, result.get(0));
        assertEquals(thirdUser, result.get(1));

        final List<User> empty = daoFactory.getUserDao().getByIds(Collections.emptyList());
        assertEquals(0, empty.size());
    }

    @Test
    void getByLogin() {
        UserAccount userAccount = daoFactory.getUserAccountDao().create(TestUtils.getTestUser(), TestUtils.getTestUserAccount());
        User createdUser = daoFactory.getUserDao().getAll().get(0);
        assertNotNull(createdUser.getId());

        final User bdUser = daoFactory.getUserDao().getByLogin(createdUser.getLogin());
        assertEquals(createdUser, bdUser);
    }

    @Test
    void getByEmail() {
        UserAccount userAccount = daoFactory.getUserAccountDao().create(TestUtils.getTestUser(), TestUtils.getTestUserAccount());
        User createdUser = daoFactory.getUserDao().getAll().get(0);
        assertNotNull(createdUser.getId());

        final User bdUser = daoFactory.getUserDao().getByEmail(createdUser.getEmail());
        assertEquals(createdUser, bdUser);
    }

    @Test
    void getAll() {
        final User user = new User();
        user.setLogin("login1");
        user.setEmail("email1@gmail.com");

        User testUser = TestUtils.getTestUser();

        daoFactory.getUserAccountDao().create(testUser, TestUtils.getTestUserAccount());
        daoFactory.getUserAccountDao().create(user, TestUtils.getTestUserAccount());

        final List<User> all = daoFactory.getUserDao().getAll();
        assertEquals(2, all.size());
        assertEquals(all.get(0).getLogin(), testUser.getLogin());
        assertEquals(all.get(1).getLogin(), user.getLogin());
    }

    @Test
    void update() {
        final User user = new User();
        user.setLogin("q");
        user.setEmail("w");

        final User createdUser = createUser(user);

        user.setId(createdUser.getId());
        user.setLogin("login1");
        user.setFirstName("firstName1");
        user.setLastName("lastName1");
        user.setEmail("email1@gmail.com");
        user.setPhone("88005553534");
        user.setAbout("about1");
        user.setAvatar("avatar1");
        user.setCreatedAt(Instant.now());

        daoFactory.getUserDao().update(user);

        final User bdUser = daoFactory.getUserDao().getById(createdUser.getId());
        assertNotNull(createdUser.getId());
        assertEquals("login1", bdUser.getLogin());
        assertEquals("firstName1", bdUser.getFirstName());
        assertEquals("lastName1", bdUser.getLastName());
        assertEquals("email1@gmail.com", bdUser.getEmail());
        assertEquals("88005553534", bdUser.getPhone());
        assertEquals("about1", bdUser.getAbout());
        assertEquals("avatar1", bdUser.getAvatar());
        assertEquals(createdUser.getCreatedAt(), bdUser.getCreatedAt());
    }
}
