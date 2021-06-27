package me.becycled.backend.model.entity.route;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * @author I1yi4
 */
public final class Route {

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

    //endregion GETTERS & SETTERS

    @Override
    @SuppressWarnings("CyclomaticComplexity")
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Route)) {
            return false;
        }
        final Route that = (Route) o;
        return Objects.equals(id, that.id)
            && Objects.equals(userId, that.userId)
            && Objects.equals(name, that.name)
            && Objects.equals(routeInfo, that.routeInfo)
            && Objects.equals(routePreview, that.routePreview)
            && Objects.equals(sportTypes, that.sportTypes)
            && Objects.equals(disposable, that.disposable)
            && Objects.equals(description, that.description)
            && Objects.equals(popularity, that.popularity)
            && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, name,
            routeInfo, routePreview, sportTypes, disposable,
            description, popularity, createdAt);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
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
