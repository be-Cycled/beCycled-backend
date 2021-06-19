package me.becycled.backend.model.entity.route;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.Objects;

/**
 * @author I1yi4
 */
public class RoutePhoto {

    private Integer id;
    private Integer routeId;
    private String photo;
    private Instant createdAt;

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
        return new ToStringBuilder(this)
            .append("id", id)
            .append("routeId", routeId)
            .append("photo", photo)
            .append("createdAt", createdAt)
            .toString();
    }
}
