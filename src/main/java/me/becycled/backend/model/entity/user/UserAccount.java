package me.becycled.backend.model.entity.user;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;
import java.util.Objects;

/**
 * @author I1yi4
 */
public class UserAccount {

    private Integer userId;
    private String password;
    private Instant lastAuthTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(final Integer userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Instant getLastAuthTime() {
        return lastAuthTime;
    }

    public void setLastAuthTime(final Instant lastAuthTime) {
        this.lastAuthTime = lastAuthTime;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserAccount that = (UserAccount) o;
        return Objects.equals(userId, that.userId)
            && Objects.equals(password, that.password)
            && Objects.equals(lastAuthTime, that.lastAuthTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, password, lastAuthTime);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("userId", userId)
            .append("password", password)
            .append("lastAuthTime", lastAuthTime)
            .toString();
    }
}

