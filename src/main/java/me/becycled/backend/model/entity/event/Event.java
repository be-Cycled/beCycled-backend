package me.becycled.backend.model.entity.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import me.becycled.backend.model.entity.event.bicycle.BicycleCompetition;
import me.becycled.backend.model.entity.event.bicycle.BicycleWorkout;
import me.becycled.backend.model.entity.event.rollerblade.RollerbladeCompetition;
import me.becycled.backend.model.entity.event.rollerblade.RollerbladeWorkout;
import me.becycled.backend.model.entity.event.run.RunCompetition;
import me.becycled.backend.model.entity.event.run.RunWorkout;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * @author I1yi4
 * @author binakot
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    visible = true,
    property = "eventType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = RunWorkout.class, name = "RUN_WORKOUT"),
    @JsonSubTypes.Type(value = RunCompetition.class, name = "RUN_COMPETITION"),
    @JsonSubTypes.Type(value = BicycleWorkout.class, name = "BICYCLE_WORKOUT"),
    @JsonSubTypes.Type(value = BicycleCompetition.class, name = "BICYCLE_COMPETITION"),
    @JsonSubTypes.Type(value = RollerbladeWorkout.class, name = "ROLLERBLADE_WORKOUT"),
    @JsonSubTypes.Type(value = RollerbladeCompetition.class, name = "ROLLERBLADE_COMPETITION")
})
@ApiModel(description = "Событие")
public abstract class Event {

    @ApiModelProperty(notes = "Уникальный идентификатор", required = true, position = 0)
    protected Integer id;
    @ApiModelProperty(notes = "Идентификатор пользователя, который создал событие", required = true, position = 1)
    protected Integer ownerUserId;
    @ApiModelProperty(notes = "Идентификатор сообщества, в рамках которого проводится событие", required = true, position = 2)
    protected Integer communityId;

    @ApiModelProperty(notes = "Тип события", required = true, position = 3)
    protected EventType eventType;

    @ApiModelProperty(notes = "Время начала события", required = true, position = 4)
    protected Instant startDate;
    @ApiModelProperty(notes = "Длительность события, секунды", required = true, position = 5)
    protected Integer duration;
    @ApiModelProperty(notes = "Описание события", required = true, position = 6)
    protected String description;
    @ApiModelProperty(notes = "Флаг приватности", required = true, position = 7)
    protected Boolean isPrivate;

    @ApiModelProperty(notes = "Идентификатор маршрута, по которому будет проходить события", required = true, position = 8)
    protected Integer routeId;
    @ApiModelProperty(notes = "Место сбора", required = true, position = 9)
    protected String venueGeoData;

    @ApiModelProperty(notes = "Список идентификаторов пользователей, участвующих в событии", required = true, position = 10)
    protected List<Integer> memberUserIds;

    @ApiModelProperty(notes = "Время создания", required = true, position = 11)
    protected Instant createdAt;

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

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(final Instant startDate) {
        this.startDate = startDate;
    }

    public Integer getDuration() {
        return duration;
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

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(final Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(final Integer routeId) {
        this.routeId = routeId;
    }

    public String getVenueGeoData() {
        return venueGeoData;
    }

    public void setVenueGeoData(final String venueGeoData) {
        this.venueGeoData = venueGeoData;
    }

    public List<Integer> getMemberUserIds() {
        return memberUserIds;
    }

    public void setMemberUserIds(final List<Integer> memberUserIds) {
        this.memberUserIds = memberUserIds;
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
            && eventType == that.eventType
            && Objects.equals(startDate, that.startDate)
            && Objects.equals(duration, that.duration)
            && Objects.equals(description, that.description)
            && Objects.equals(isPrivate, that.isPrivate)
            && Objects.equals(routeId, that.routeId)
            && Objects.equals(venueGeoData, that.venueGeoData)
            && Objects.equals(memberUserIds, that.memberUserIds)
            && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerUserId, communityId, eventType,
            startDate, duration, description, isPrivate,
            routeId, venueGeoData, memberUserIds, createdAt);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("ownerUserId", ownerUserId)
            .append("communityId", communityId)
            .append("eventType", eventType)
            .append("startDate", startDate)
            .append("duration", duration)
            .append("description", description)
            .append("isPrivate", isPrivate)
            .append("routeId", routeId)
            .append("venueGeoData", venueGeoData)
            .append("memberUserIds", memberUserIds)
            .append("createdAt", createdAt)
            .toString();
    }
}
