package me.becycled.backend.model.entity.user;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;
import java.util.Objects;

/**
 * @author I1yi4
 */
public final class User {

    private Integer id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String about;
    private String avatar;
    private Instant createdAt;

    //region GETTERS & SETTERS

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(final String about) {
        this.about = about;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(final String avatar) {
        this.avatar = avatar;
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
        final User that = (User) o;
        return Objects.equals(id, that.id)
            && Objects.equals(login, that.login)
            && Objects.equals(firstName, that.firstName)
            && Objects.equals(lastName, that.lastName)
            && Objects.equals(email, that.email)
            && Objects.equals(phone, that.phone)
            && Objects.equals(about, that.about)
            && Objects.equals(avatar, that.avatar)
            && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, firstName, lastName, email, phone, about, avatar, createdAt);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("login", login)
            .append("firstName", firstName)
            .append("lastName", lastName)
            .append("email", email)
            .append("phone", phone)
            .append("about", about)
            .append("avatar", avatar)
            .append("createdAt", createdAt)
            .toString();
    }
}
