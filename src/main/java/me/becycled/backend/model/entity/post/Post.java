package me.becycled.backend.model.entity.post;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.Objects;

/**
 * @author Suren Kalaychyan
 */
public final class Post {

    private Integer id;
    private Integer userId;
    private String title;
    private String content;
    private String poster;
    private Instant createdAt;
    private Instant updatedAt;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(final String poster) {
        this.poster = poster;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Post post = (Post) o;
        return Objects.equals(id, post.id)
            && Objects.equals(userId, post.userId)
            && Objects.equals(title, post.title)
            && Objects.equals(content, post.content)
            && Objects.equals(poster, post.poster)
            && Objects.equals(createdAt, post.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, title, content, poster, createdAt);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("userId", userId)
            .append("title", title)
            .append("content", content)
            .append("poster", poster)
            .append("createdAt", createdAt)
            .append("updatedAt", updatedAt)
            .toString();
    }
}
