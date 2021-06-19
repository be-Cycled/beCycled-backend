package me.becycled.backend.model.entity.community;

import me.becycled.backend.model.entity.route.SportType;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * @author I1yi4
 */
public class Community {

    private Integer id;
    private Integer ownerUserId;
    private String name;
    private String nickname;
    private String avatar;
    private CommunityType communityType;
    private List<SportType> sportTypes;
    private List<Integer> userIds;
    private String url;
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

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(final String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(final String avatar) {
        this.avatar = avatar;
    }

    public CommunityType getCommunityType() {
        return communityType;
    }

    public void setCommunityType(final CommunityType communityType) {
        this.communityType = communityType;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
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
        final Community that = (Community) o;
        return Objects.equals(id, that.id)
            && Objects.equals(ownerUserId, that.ownerUserId)
            && Objects.equals(name, that.name)
            && Objects.equals(nickname, that.nickname)
            && Objects.equals(avatar, that.avatar)
            && communityType == that.communityType
            && Objects.equals(sportTypes, that.sportTypes)
            && Objects.equals(userIds, that.userIds)
            && Objects.equals(url, that.url)
            && Objects.equals(description, that.description)
            && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerUserId, name, nickname, avatar, communityType, sportTypes, userIds, url, description, createdAt);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("ownerUserId", ownerUserId)
            .append("name", name)
            .append("nickname", nickname)
            .append("avatar", avatar)
            .append("communityType", communityType)
            .append("sportTypes", sportTypes)
            .append("userIds", userIds)
            .append("url", url)
            .append("description", description)
            .append("createdAt", createdAt)
            .toString();
    }
}
