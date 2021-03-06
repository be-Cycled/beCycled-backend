package me.becycled.backend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

/**
 * @author I1yi4
 */
@ApiModel(description = "Регистрационные данные пользователя")
public final class UserRegistrationDto {

    @ApiModelProperty(notes = "Логин", required = true, position = 0)
    private String login;
    @ApiModelProperty(notes = "Пароль", required = true, position = 1)
    private String password;
    @ApiModelProperty(notes = "Электронная почта", required = true, position = 2)
    private String email;

    //region GETTERS & SETTERS

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

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
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
        final UserRegistrationDto that = (UserRegistrationDto) o;
        return Objects.equals(login, that.login)
            && Objects.equals(password, that.password)
            && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, email);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("login", login)
            .append("password", "*** hidden ***")
            .append("email", email)
            .toString();
    }
}
