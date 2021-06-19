package me.becycled.backend.model.entity.competition;

import me.becycled.backend.model.entity.route.SportType;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * @author I1yi4
 */
public final class Competition {

    private Integer id;
    private Integer ownerUserId;
    private Integer communityId;
    private Boolean isPrivate;
    private Instant startDate;
    private Integer routeId;
    private List<SportType> sportTypes;
    private List<Integer> userIds;
    private String venue;
    private Integer duration;
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

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(final Boolean isPrivate) {
        this.isPrivate = isPrivate;
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

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(final Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(final String venue) {
        this.venue = venue;
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
        final Competition competition = (Competition) o;
        return Objects.equals(id, competition.id)
            && Objects.equals(ownerUserId, competition.ownerUserId)
            && Objects.equals(communityId, competition.communityId)
            && Objects.equals(isPrivate, competition.isPrivate)
            && Objects.equals(startDate, competition.startDate)
            && Objects.equals(routeId, competition.routeId)
            && Objects.equals(sportTypes, competition.sportTypes)
            && Objects.equals(userIds, competition.userIds)
            && Objects.equals(venue, competition.venue)
            && Objects.equals(duration, competition.duration)
            && Objects.equals(description, competition.description)
            && Objects.equals(createdAt, competition.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerUserId, communityId, isPrivate, startDate, routeId, sportTypes, userIds, venue, duration, description, createdAt);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("ownerUserId", ownerUserId)
            .append("communityId", communityId)
            .append("isPrivate", isPrivate)
            .append("startDate", startDate)
            .append("routeId", routeId)
            .append("sportTypes", sportTypes)
            .append("userIds", userIds)
            .append("description", description)
            .append("venue", venue)
            .append("duration", duration)
            .append("createdAt", createdAt)
            .toString();
    }
}
