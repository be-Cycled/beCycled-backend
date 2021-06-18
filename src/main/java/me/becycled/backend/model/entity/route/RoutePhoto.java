package me.becycled.backend.model.entity.route;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author I1yi4
 */
public class RoutePhoto {

    private Integer id;
    private Integer routeId;
    private byte[] photo;
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

    @SuppressWarnings("PMD.MethodReturnsInternalArray")
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public byte[] getPhoto() {
        return photo;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setPhoto(final byte[] photo) {
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
            && Arrays.equals(photo, that.photo)
            && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, routeId, createdAt);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
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
