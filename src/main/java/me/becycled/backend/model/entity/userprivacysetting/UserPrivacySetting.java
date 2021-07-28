package me.becycled.backend.model.entity.userprivacysetting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;
import java.util.Objects;

/**
 * @author I1yi4
 */
@ApiModel(description = "Настройки приватности пользователей")
public class UserPrivacySetting {

    @ApiModelProperty(notes = "Уникальный идентификатор пользователя", required = true, position = 0)
    private Integer userId;

    @ApiModelProperty(notes = "Ассоциативный массив где ключ это название поля пользователя а значение правило доступа к полю", required = true, position = 1)
    private Map<String, PrivacyRule> privacySettings;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(final Integer userId) {
        this.userId = userId;
    }

    public Map<String, PrivacyRule> getPrivacySettings() {
        return privacySettings;
    }

    public void setPrivacySettings(final Map<String, PrivacyRule> privacySettings) {
        this.privacySettings = privacySettings;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserPrivacySetting that = (UserPrivacySetting) o;
        return Objects.equals(userId, that.userId) && Objects.equals(privacySettings, that.privacySettings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, privacySettings);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("userId", userId)
            .append("privacySettings", privacySettings)
            .toString();
    }
}
