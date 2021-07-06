package me.becycled.backend.model.entity.route;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;
import java.util.Objects;

/**
 * @author I1yi4
 */
@ApiModel(description = "Фотография маршрута")
public final class RoutePhoto {

    @ApiModelProperty(notes = "Уникальный идентификатор", required = true, position = 0)
    private Integer id;
    @ApiModelProperty(notes = "Идентификатор маршрута, к которому относится фотография", required = true, position = 1)
    private Integer routeId;
    @ApiModelProperty(notes = "Фотография", required = true, position = 2)
    private String photo;
    @ApiModelProperty(notes = "Время создания", required = true, position = 3)
    private Instant createdAt;

    //region GETTERS & SETTERS

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(final Integer routeId) {
        this.routeId = routeId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(final String photo) {
        this.photo = photo;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    //endregion GETTERS & SETTERS

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoutePhoto)) {
            return false;
        }
        final RoutePhoto that = (RoutePhoto) o;
        return Objects.equals(id, that.id)
            && Objects.equals(routeId, that.routeId)
            && Objects.equals(photo, that.photo)
            && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, routeId, photo, createdAt);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("routeId", routeId)
            .append("photo", photo)
            .append("createdAt", createdAt)
            .toString();
    }
}
