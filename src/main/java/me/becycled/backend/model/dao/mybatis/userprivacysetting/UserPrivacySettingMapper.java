package me.becycled.backend.model.dao.mybatis.userprivacysetting;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.becycled.backend.model.entity.userprivacysetting.UserPrivacySetting;
import me.becycled.backend.model.utils.JsonUtils;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.ResultHandler;

import java.util.Map;

/**
 * @author I1yi4
 */
@SuppressWarnings("PMD.LinguisticNaming")
public interface UserPrivacySettingMapper {

    @InsertProvider(type = UserPrivacySettingMapper.UserPrivacySettingSqlBuilder.class, method = "buildCreateSql")
    int create(UserPrivacySetting userPrivacySetting);

    @InsertProvider(type = UserPrivacySettingMapper.UserPrivacySettingSqlBuilder.class, method = "buildUpdateSql")
    int update(UserPrivacySetting userPrivacySetting);

    @Results(id = "userPrivacySettingResult", value = {
        @Result(column = "user_id", property = "userId"),
        @Result(column = "privacy_settings", property = "privacySettings")
    })
    @Select("SELECT * FROM user_privacy_settings WHERE user_id=#{userId}")
    @ResultType(Map.class)
    void getByUserId(Integer userId, ResultHandler<?> handler);

    /**
     * @author I1yi4
     */
    class UserPrivacySettingSqlBuilder {
        private static final String TABLE_NAME = "user_privacy_settings";

        @SuppressWarnings("PMD.DoubleBraceInitialization")
        public String buildCreateSql(final UserPrivacySetting userPrivacySetting) throws JsonProcessingException {
            return new SQL() {{
                INSERT_INTO(TABLE_NAME);

                INTO_COLUMNS("user_id, privacy_settings");
                INTO_VALUES("#{userId}");
                INTO_VALUES("'" + JsonUtils.getJsonMapper().writeValueAsString(userPrivacySetting.getPrivacySettings()) + "'::JSONB");

            }}.toString();
        }

        @SuppressWarnings("PMD.DoubleBraceInitialization")
        public String buildUpdateSql(final UserPrivacySetting userPrivacySetting) throws JsonProcessingException {
            return new SQL() {{
                UPDATE(TABLE_NAME);

                SET("privacy_settings = '" + JsonUtils.getJsonMapper().writeValueAsString(userPrivacySetting.getPrivacySettings()) + "'::JSONB");

                WHERE("user_id = #{userId}");

            }}.toString();
        }
    }
}
