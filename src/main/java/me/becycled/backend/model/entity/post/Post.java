package me.becycled.backend.model.entity.post;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;
import java.util.Objects;

/**
 * @author Suren Kalaychyan
 */
@ApiModel(description = "Статья")
public final class Post {

    @ApiModelProperty(notes = "Уникальный идентификатор", required = true, position = 0)
    private Integer id;
    @ApiModelProperty(notes = "Идентификатор пользователя, который опубликовал статью", required = true, position = 1)
    private Integer userId;
    @ApiModelProperty(notes = "Заголовок статьи", required = true, position = 2)
    private String title;
    @ApiModelProperty(notes = "Содержание статьи", required = true, position = 3)
    private String content;
    @ApiModelProperty(notes = "Изображение", required = true, position = 4)
    private String poster;
    @ApiModelProperty(notes = "Время создания", required = true, position = 5)
    private Instant createdAt;
    @ApiModelProperty(notes = "Время обновления", required = true, position = 6)
    private Instant updatedAt;

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

    //endregion GETTERS & SETTERS

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Post that = (Post) o;
        return Objects.equals(id, that.id)
            && Objects.equals(userId, that.userId)
            && Objects.equals(title, that.title)
            && Objects.equals(content, that.content)
            && Objects.equals(poster, that.poster)
            && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, title, content, poster, createdAt);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
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
