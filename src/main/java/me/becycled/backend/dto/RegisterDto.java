package me.becycled.backend.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

/**
 * @author I1yi4
 */
public class RegisterDto {

    private String email;
    private String login;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RegisterDto registerDto = (RegisterDto) o;
        return Objects.equals(email, registerDto.email)
            && Objects.equals(login, registerDto.login)
            && Objects.equals(password, registerDto.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, login, password);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("email", email)
            .append("login", login)
            .append("password", password)
            .toString();
    }
}
