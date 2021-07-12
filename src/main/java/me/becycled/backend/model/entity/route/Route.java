package me.becycled.backend.model.entity.route;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * @author I1yi4
 */
@ApiModel(description = "Маршрут")
public final class Route {

    @ApiModelProperty(notes = "Уникальный идентификатор", required = true, position = 0)
    private Integer id;
    @ApiModelProperty(notes = "Идентификатор пользователя, который создал маршрут", required = true, position = 1)
    private Integer userId;
    @ApiModelProperty(notes = "Название маршрута", required = true, position = 2)
    private String name;
    @ApiModelProperty(notes = "Данные маршрута", required = true, position = 3)
    private String routeGeoData;
    @ApiModelProperty(notes = "Изображение превью маршрута", required = true, position = 4)
    private String routePreview;
    @ApiModelProperty(notes = "Список видов спорта", required = true, position = 5)
    private List<SportType> sportTypes;
    @ApiModelProperty(notes = "Флаг разового использования маршрута", required = true, position = 6)
    private Boolean disposable;
    @ApiModelProperty(notes = "Описание маршрута", required = true, position = 7)
    private String description;
    @ApiModelProperty(notes = "Рейтинг популярности маршрута", required = true, position = 8)
    private Integer popularity;
    @ApiModelProperty(notes = "Время создаения", required = true, position = 9)
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

    public String getRouteGeoData() {
        return routeGeoData;
    }

    public void setRouteGeoData(final String routeGeoData) {
        this.routeGeoData = routeGeoData;
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
            && Objects.equals(routeGeoData, that.routeGeoData)
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
            routeGeoData, routePreview, sportTypes, disposable,
            description, popularity, createdAt);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("userId", userId)
            .append("name", name)
            .append("routeInfo", routeGeoData)
            .append("routePreview", routePreview)
            .append("sportTypes", sportTypes)
            .append("disposable", disposable)
            .append("description", description)
            .append("popularity", popularity)
            .append("createdAt", createdAt)
            .toString();
    }
}
