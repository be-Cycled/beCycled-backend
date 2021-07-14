package me.becycled.backend.model.entity.event;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import me.becycled.backend.model.entity.route.SportType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * @author I1yi4
 */
@ApiModel(description = "Событие")
public final class Event {

    @ApiModelProperty(notes = "Уникальный идентификатор", required = true, position = 0)
    private Integer id;
    @ApiModelProperty(notes = "Идентификатор пользователя, который создал событие", required = true, position = 1)
    private Integer ownerUserId;
    @ApiModelProperty(notes = "Идентификатор сообщества, в рамках которого проводится событие", required = true, position = 2)
    private Integer communityId;
    @ApiModelProperty(notes = "Тип события", required = true, position = 3)
    private EventType eventType;
    @ApiModelProperty(notes = "Флаг приватности", required = true, position = 4)
    private Boolean isPrivate;
    @ApiModelProperty(notes = "Время начала события", required = true, position = 5)
    private Instant startDate;
    @ApiModelProperty(notes = "Идентификатор маршрута, по которому будет проходить события", required = true, position = 6)
    private Integer routeId;
    @ApiModelProperty(notes = "Вид спорта", required = true, position = 7)
    private SportType sportType;
    @ApiModelProperty(notes = "Список идентификаторов пользователей, участвующих в событии", required = true, position = 8)
    private List<Integer> userIds;
    @ApiModelProperty(notes = "Место сбора", required = true, position = 9)
    private String venueGeoData;
    @ApiModelProperty(notes = "Длительность события, секунды", required = true, position = 10)
    private Integer duration;
    @ApiModelProperty(notes = "Описание события", required = true, position = 11)
    private String description;
    @ApiModelProperty(notes = "Время создания", required = true, position = 12)
    private Instant createdAt;

    //region GETTERS & SETTERS

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(final Integer ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(final Integer communityId) {
        this.communityId = communityId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(final EventType eventType) {
        this.eventType = eventType;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(final Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(final Instant startDate) {
        this.startDate = startDate;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(final Integer routeId) {
        this.routeId = routeId;
    }

    public SportType getSportType() {
        return sportType;
    }

    public void setSportType(final SportType sportType) {
        this.sportType = sportType;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(final List<Integer> userIds) {
        this.userIds = userIds;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getVenueGeoData() {
        return venueGeoData;
    }

    public void setVenueGeoData(final String venueGeoData) {
        this.venueGeoData = venueGeoData;
    }

    public void setDuration(final Integer duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Event that = (Event) o;
        return Objects.equals(id, that.id)
            && Objects.equals(ownerUserId, that.ownerUserId)
            && Objects.equals(communityId, that.communityId)
            && Objects.equals(eventType, that.eventType)
            && Objects.equals(isPrivate, that.isPrivate)
            && Objects.equals(startDate, that.startDate)
            && Objects.equals(routeId, that.routeId)
            && Objects.equals(sportType, that.sportType)
            && Objects.equals(userIds, that.userIds)
            && Objects.equals(venueGeoData, that.venueGeoData)
            && Objects.equals(duration, that.duration)
            && Objects.equals(description, that.description)
            && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerUserId, eventType, communityId, isPrivate,
            startDate, routeId, sportType, userIds, description, createdAt);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("ownerUserId", ownerUserId)
            .append("communityId", communityId)
            .append("eventType", eventType)
            .append("isPrivate", isPrivate)
            .append("startDate", startDate)
            .append("routeId", routeId)
            .append("sportTypes", sportType)
            .append("userIds", userIds)
            .append("description", description)
            .append("venueGeoData", venueGeoData)
            .append("duration", duration)
            .append("createdAt", createdAt)
            .toString();
    }
}
