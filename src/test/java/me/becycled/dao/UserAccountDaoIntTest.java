package me.becycled.dao;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.user.UserAccount;
import me.becycled.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Suren Kalaychyan
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserAccountDaoIntTest extends BaseIntegrationTest {

    @BeforeEach
    public void setUp() {
        daoFactory.getUserDao().create(TestUtils.getTestUser());
        daoFactory.getRouteDao().create(TestUtils.getTestRoute());
        daoFactory.getCommunityDao().create(TestUtils.getTestCommunity());
    }


    @Test
    void getByUserId() {
        final UserAccount createUserAccount = daoFactory.getUserAccountDao().create(TestUtils.getTestUserAccount());
        final UserAccount dbUserAccount = daoFactory.getUserAccountDao().getByUserId(createUserAccount.getUserId());

        assertEquals(createUserAccount.getUserId(), dbUserAccount.getUserId());
        assertEquals(createUserAccount.getPassword(), dbUserAccount.getPassword());
        assertEquals(createUserAccount.getLastAuthTime(), dbUserAccount.getLastAuthTime());
    }

    @Test
    void create() {
        final UserAccount testUserAccount = TestUtils.getTestUserAccount();
        final UserAccount dbUserAccount = daoFactory.getUserAccountDao().create(testUserAccount);

        assertEquals(testUserAccount.getUserId(), dbUserAccount.getUserId());
        assertEquals(testUserAccount.getPassword(), dbUserAccount.getPassword());
        assertEquals(testUserAccount.getLastAuthTime(), dbUserAccount.getLastAuthTime());
    }

    @Test
    void update() {
        final UserAccount testUserAccount = TestUtils.getTestUserAccount();
        final UserAccount dbUserAccount = daoFactory.getUserAccountDao().create(testUserAccount);

        assertEquals(testUserAccount.getUserId(), dbUserAccount.getUserId());
        assertEquals(testUserAccount.getPassword(), dbUserAccount.getPassword());
        assertEquals(testUserAccount.getLastAuthTime(), dbUserAccount.getLastAuthTime());

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
