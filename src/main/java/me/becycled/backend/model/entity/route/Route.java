package me.becycled.backend.model.entity.route;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * @author I1yi4
 */
public class Route {

    private Integer id;
    private Integer userId;
    private String name;
    private String routeInfo;
    private String routePreview;
    private List<SportType> sportTypes;
    private Boolean disposable;
    private String description;
    private Integer popularity;
    private Instant createdAt;

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

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(final String routeInfo) {
        this.routeInfo = routeInfo;
    }

    public String getRoutePreview() {
        return routePreview;
    }

    public void setRoutePreview(final String routePreview) {
        this.routePreview = routePreview;
    }

    public List<SportType> getSportTypes() {
        return sportTypes;
    }

    public void setSportTypes(final List<SportType> sportTypes) {
        this.sportTypes = sportTypes;
    }

    public Boolean getDisposable() {
        return disposable;
    }

    public void setDisposable(final Boolean disposable) {
        this.disposable = disposable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(final Integer popularity) {
        this.popularity = popularity;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    @SuppressWarnings("CyclomaticComplexity")
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Route)) {
            return false;
        }
        final Route route = (Route) o;
        return Objects.equals(id, route.id)
            && Objects.equals(userId, route.userId)
            && Objects.equals(name, route.name)
            && Objects.equals(routeInfo, route.routeInfo)
            && Objects.equals(routePreview, route.routePreview)
            && Objects.equals(sportTypes, route.sportTypes)
            && Objects.equals(disposable, route.disposable)
            && Objects.equals(description, route.description)
            && Objects.equals(popularity, route.popularity)
            && Objects.equals(createdAt, route.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, name, routeInfo, routePreview, sportTypes, disposable, description, popularity, createdAt);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("userId", userId)
            .append("name", name)
            .append("routeInfo", routeInfo)
            .append("routePreview", routePreview)
            .append("sportTypes", sportTypes)
            .append("disposable", disposable)
            .append("description", description)
            .append("popularity", popularity)
            .append("createdAt", createdAt)
            .toString();
    }
}
