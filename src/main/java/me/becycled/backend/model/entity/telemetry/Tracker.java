package me.becycled.backend.model.entity.telemetry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

/**
 * @author binakot
 */
@ApiModel(description = "Трекер")
public final class Tracker {

    @ApiModelProperty(notes = "Идентификатор трекера", required = true, position = 0)
    private Integer id;
    @ApiModelProperty(notes = "Идентификатор пользователя, к которому привязан трекер", required = true, position = 1)
    private Integer userId;
    @ApiModelProperty(notes = "IMEI устройства", required = true, position = 2)
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
