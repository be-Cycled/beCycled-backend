package me.becycled.backend.model.entity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author I1yi4
 */
public class User {

    private Integer id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String about;
    private byte[] avatar;
    private Instant createdAt;

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

    @SuppressWarnings("PMD.MethodReturnsInternalArray")
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public byte[] getAvatar() {
        return avatar;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setAvatar(final byte[] avatar) {
        this.avatar = avatar;
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
        final User user = (User) o;
        return Objects.equals(id, user.id)
            && Objects.equals(login, user.login)
            && Objects.equals(firstName, user.firstName)
            && Objects.equals(lastName, user.lastName)
            && Objects.equals(email, user.email)
            && Objects.equals(phone, user.phone)
            && Objects.equals(about, user.about)
            && Arrays.equals(avatar, user.avatar)
            && Objects.equals(createdAt, user.createdAt);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, login, firstName, lastName, email, phone, about, createdAt);
        result = 31 * result + Arrays.hashCode(avatar);
        return result;
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

