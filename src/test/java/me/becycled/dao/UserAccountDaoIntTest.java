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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Suren Kalaychyan
 * @author I1yi4
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserAccountDaoIntTest extends BaseIntegrationTest {

    @Test
    void getByUserId() {
        final UserAccount createUserAccount = daoFactory.getUserAccountDao().create(TestUtils.getTestUser(), TestUtils.getTestUserAccount());
        final UserAccount dbUserAccount = daoFactory.getUserAccountDao().getByUserId(createUserAccount.getUserId());

        assertEquals(createUserAccount.getUserId(), dbUserAccount.getUserId());
        assertEquals(createUserAccount.getPassword(), dbUserAccount.getPassword());
        assertEquals(createUserAccount.getLastAuthTime(), dbUserAccount.getLastAuthTime());
    }

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

        final UserAccount userAccount = new UserAccount();
        userAccount.setUserId(100500);
        userAccount.setPassword("password");
        userAccount.setLastAuthTime(Instant.parse("2021-06-18T12:00:00Z"));

        final UserAccount dbUserAccount = daoFactory.getUserAccountDao().create(user, userAccount);

        List<User> users = daoFactory.getUserDao().getAll();
        assertEquals(1, users.size());
        User createdUser = users.get(0);

        assertNotNull(createdUser.getId());
        assertEquals(user.getLogin(), createdUser.getLogin());
        assertEquals(user.getFirstName(), createdUser.getFirstName());
        assertEquals(user.getLastName(), createdUser.getLastName());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getPhone(), createdUser.getPhone());
        assertEquals(user.getAbout(), createdUser.getAbout());
        assertEquals(user.getAvatar(), createdUser.getAvatar());
        assertNotNull(createdUser.getCreatedAt());

        assertEquals(createdUser.getId(), dbUserAccount.getUserId());
        assertEquals(userAccount.getPassword(), dbUserAccount.getPassword());
        assertNull(dbUserAccount.getLastAuthTime());
    }

    @Test
    void update() {
        final UserAccount testUserAccount = TestUtils.getTestUserAccount();
        final UserAccount dbUserAccount = daoFactory.getUserAccountDao().create(TestUtils.getTestUser(), testUserAccount);

        assertEquals(testUserAccount.getUserId(), dbUserAccount.getUserId());
        assertEquals(testUserAccount.getPassword(), dbUserAccount.getPassword());
        assertNull(dbUserAccount.getLastAuthTime());

        final UserAccount update = daoFactory.getUserAccountDao().getByUserId(testUserAccount.getUserId());
        update.setPassword("NEW_PASSWORD");
        update.setLastAuthTime(Instant.now());
        daoFactory.getUserAccountDao().update(update);

        final UserAccount afterUpdate = daoFactory.getUserAccountDao().getByUserId(testUserAccount.getUserId());
        assertEquals(afterUpdate.getUserId(), dbUserAccount.getUserId());
        assertEquals(afterUpdate.getPassword(), "NEW_PASSWORD");
        assertEquals(afterUpdate.getLastAuthTime(), dbUserAccount.getLastAuthTime());
    }
}
