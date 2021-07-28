package me.becycled.backend.model.dao.mybatis.userprivacysetting;

import me.becycled.backend.model.dao.mybatis.BaseMyBatisDao;
import me.becycled.backend.model.entity.userprivacysetting.PrivacyRule;
import me.becycled.backend.model.entity.userprivacysetting.UserPrivacySetting;
import me.becycled.backend.model.utils.JsonUtils;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author I1yi4
 */
public class UserPrivacySettingDao extends BaseMyBatisDao {

    public UserPrivacySettingDao(final SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    public UserPrivacySetting getByUserId(final Integer userId) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final UserPrivacySettingMapper mapper = session.getMapper(UserPrivacySettingMapper.class);
            final UserPrivacySettingResultHandler resultHandler = new UserPrivacySettingResultHandler();
            mapper.getByUserId(userId, resultHandler);

            return resultHandler.getResult();
        }
    }

    public UserPrivacySetting create(final UserPrivacySetting userPrivacySetting) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final UserPrivacySettingMapper mapper = session.getMapper(UserPrivacySettingMapper.class);

            mapper.create(userPrivacySetting);

            return userPrivacySetting;
        }
    }

    public UserPrivacySetting update(final UserPrivacySetting userPrivacySetting) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final UserPrivacySettingMapper mapper = session.getMapper(UserPrivacySettingMapper.class);

            mapper.update(userPrivacySetting);

            return userPrivacySetting;
        }
    }

    private static final class UserPrivacySettingResultHandler implements ResultHandler {

        private UserPrivacySetting result;

        @Override
        @SuppressWarnings({"unchecked", "PMD.EmptyCatchBlock", "PMD.AvoidThrowingRawExceptionTypes"})
        public void handleResult(final ResultContext rc) {
            final Map<String, Object> convertedRow = (Map<String, Object>) rc.getResultObject();
            try {
                result = new UserPrivacySetting();
                result.setUserId((Integer) convertedRow.get("userId"));
                final Map<String, String> privacySettings = (Map<String, String>) JsonUtils.getJsonMapper().readValue((String) convertedRow.get("privacySettings"), Map.class);

                result.setPrivacySettings(privacySettings.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> PrivacyRule.valueOf(e.getValue()))));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        private UserPrivacySetting getResult() {
            return result;
        }
    }
}
