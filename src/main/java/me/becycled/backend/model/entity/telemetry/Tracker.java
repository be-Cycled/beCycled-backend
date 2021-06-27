package me.becycled.backend.model.entity.telemetry;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

/**
 * @author binakot
 */
public final class Tracker {

    private Integer id;
    private Integer userId;
    private String imei;

    //region GETTERS & SETTERS

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(final Integer userId) {
        this.userId = userId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(final String imei) {
        this.imei = imei;
    }

    //endregion GETTERS & SETTERS

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Tracker that = (Tracker) o;
        return Objects.equals(id, that.id)
            && Objects.equals(userId, that.userId)
            && Objects.equals(imei, that.imei);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, imei);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("userId", userId)
            .append("imei", imei)
            .toString();
    }
}
