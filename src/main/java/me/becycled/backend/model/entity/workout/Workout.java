package me.becycled.backend.model.entity.workout;

import me.becycled.backend.model.entity.route.SportType;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * @author I1yi4
 */
public class Workout {

    private Integer id;
    private Integer ownerUserId;
    private Integer communityId;
    private Boolean privateWorkout;
    private Instant startDate;
    private Integer routeId;
    private List<SportType> sportTypes;
    private List<Integer> userIds;
    private String description;
    private Instant createdAt;

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

    public Boolean getPrivateWorkout() {
        return privateWorkout;
    }

    public void setPrivateWorkout(final Boolean privateWorkout) {
        this.privateWorkout = privateWorkout;
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

    public List<SportType> getSportTypes() {
        return sportTypes;
    }

    public void setSportTypes(final List<SportType> sportTypes) {
        this.sportTypes = sportTypes;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(final List<Integer> userIds) {
        this.userIds = userIds;
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

    @Override
    @SuppressWarnings("CyclomaticComplexity")
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Workout workout = (Workout) o;
        return Objects.equals(id, workout.id) && Objects.equals(ownerUserId, workout.ownerUserId) && Objects.equals(communityId, workout.communityId) && Objects.equals(privateWorkout, workout.privateWorkout) && Objects.equals(startDate, workout.startDate) && Objects.equals(routeId, workout.routeId) && Objects.equals(sportTypes, workout.sportTypes) && Objects.equals(userIds, workout.userIds) && Objects.equals(description, workout.description) && Objects.equals(createdAt, workout.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerUserId, communityId, privateWorkout, startDate, routeId, sportTypes, userIds, description, createdAt);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("ownerUserId", ownerUserId)
            .append("communityId", communityId)
            .append("privateWorkout", privateWorkout)
            .append("startDate", startDate)
            .append("routeId", routeId)
            .append("sportTypes", sportTypes)
            .append("userIds", userIds)
            .append("description", description)
            .append("createdAt", createdAt)
            .toString();
    }
}