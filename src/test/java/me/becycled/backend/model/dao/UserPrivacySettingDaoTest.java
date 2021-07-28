package me.becycled.backend.model.dao;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.user.UserAccount;
import me.becycled.backend.model.entity.userprivacysetting.PrivacyRule;
import me.becycled.backend.model.entity.userprivacysetting.UserPrivacySetting;
import me.becycled.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author I1yi4
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserPrivacySettingDaoTest extends BaseIntegrationTest {

    @Test
    void create() {
        UserAccount userAccount = daoFactory.getUserAccountDao().create(TestUtils.getTestUser(), TestUtils.getTestUserAccount());

        final UserPrivacySetting userPrivacySetting = new UserPrivacySetting();
        userPrivacySetting.setUserId(userAccount.getUserId());
        userPrivacySetting.setPrivacySettings(Map.of(
            "avatar", PrivacyRule.OWNER_ONLY,
            "phone", PrivacyRule.OWNER_ONLY,
            "email", PrivacyRule.ALL)
        );

        UserPrivacySetting createdUserPrivacySetting = daoFactory.getUserPrivacySettingDao().create(userPrivacySetting);
        assertEquals(createdUserPrivacySetting.getUserId(), userAccount.getUserId());
        assertEquals(createdUserPrivacySetting.getPrivacySettings(), userPrivacySetting.getPrivacySettings());
    }

    @Test
    void getByUserId() {
        UserAccount userAccount = daoFactory.getUserAccountDao().create(TestUtils.getTestUser(), TestUtils.getTestUserAccount());

        final UserPrivacySetting userPrivacySetting = new UserPrivacySetting();
        userPrivacySetting.setUserId(userAccount.getUserId());
        userPrivacySetting.setPrivacySettings(Map.of(
            "avatar", PrivacyRule.OWNER_ONLY,
            "phone", PrivacyRule.OWNER_ONLY,
            "email", PrivacyRule.ALL)
        );

        daoFactory.getUserPrivacySettingDao().create(userPrivacySetting);
        UserPrivacySetting privacySetting = daoFactory.getUserPrivacySettingDao().getByUserId(userAccount.getUserId());

        assertEquals(privacySetting.getUserId(), userAccount.getUserId());
        assertEquals(userPrivacySetting.getPrivacySettings(), privacySetting.getPrivacySettings());
    }

    @Test
    void update() {
        UserAccount userAccount = daoFactory.getUserAccountDao().create(TestUtils.getTestUser(), TestUtils.getTestUserAccount());

        final UserPrivacySetting userPrivacySetting = new UserPrivacySetting();
        userPrivacySetting.setUserId(userAccount.getUserId());
        userPrivacySetting.setPrivacySettings(Map.of(
            "avatar", PrivacyRule.OWNER_ONLY,
            "phone", PrivacyRule.OWNER_ONLY,
            "email", PrivacyRule.ALL)
        );
        UserPrivacySetting createdUserPrivacySetting = daoFactory.getUserPrivacySettingDao().create(userPrivacySetting);
        assertEquals(createdUserPrivacySetting.getUserId(), userAccount.getUserId());
        assertEquals(createdUserPrivacySetting.getPrivacySettings(), userPrivacySetting.getPrivacySettings());

        final UserPrivacySetting updateSetting = new UserPrivacySetting();
        updateSetting.setUserId(userAccount.getUserId());
        updateSetting.setPrivacySettings(Map.of(
            "avatar", PrivacyRule.ALL,
            "phone", PrivacyRule.ALL,
            "email", PrivacyRule.ALL)
        );
        daoFactory.getUserPrivacySettingDao().update(updateSetting);

        UserPrivacySetting privacySetting = daoFactory.getUserPrivacySettingDao().getByUserId(userAccount.getUserId());

        assertEquals(userPrivacySetting.getUserId(), userAccount.getUserId());
        assertEquals(updateSetting.getPrivacySettings(), privacySetting.getPrivacySettings());
    }
}
