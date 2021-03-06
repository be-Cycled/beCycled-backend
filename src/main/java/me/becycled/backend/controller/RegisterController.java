package me.becycled.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.becycled.backend.dto.UserRegistrationDto;
import me.becycled.backend.exception.WrongRequestException;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.entity.user.UserAccount;
import me.becycled.backend.model.error.ErrorMessages;
import me.becycled.backend.model.utils.DomainUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author I1yi4
 */
@RestController
@RequestMapping("/register")
@Api(description = "Регистрация пользователей")
public class RegisterController {

    private final DaoFactory daoFactory;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterController(final DaoFactory daoFactory,
                              final PasswordEncoder passwordEncoder) {
        this.daoFactory = daoFactory;
        this.passwordEncoder = passwordEncoder;
    }

    @SuppressWarnings("ReturnCount")
    @RequestMapping(value = "", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Зарегистрировать нового пользователя")
    public ResponseEntity<Void> create(
        @ApiParam("Данные пользователя") @RequestBody @Validated final UserRegistrationDto entity) {

        final User userByLogin = daoFactory.getUserDao().getByLogin(entity.getLogin());
        if (userByLogin != null) {
            throw new WrongRequestException(ErrorMessages.loginAlreadyExist());
        }
        final User userByEmail = daoFactory.getUserDao().getByEmail(entity.getEmail());
        if (userByEmail != null) {
            throw new WrongRequestException(ErrorMessages.emailAlreadyExist());
        }

        final User user = new User();
        user.setLogin(entity.getLogin());
        user.setEmail(entity.getEmail());
        user.setAvatar(DomainUtils.DEFAULT_USER_AVATAR_URL);

        final UserAccount userAccount = new UserAccount();
        userAccount.setPassword(passwordEncoder.encode(entity.getPassword()));

        daoFactory.getUserAccountDao().create(user, userAccount);

        return ResponseEntity.ok().build();
    }
}
