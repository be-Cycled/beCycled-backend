package me.becycled.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.becycled.backend.exception.AuthException;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.entity.user.UserAccount;
import me.becycled.backend.model.error.ErrorMessages;
import me.becycled.backend.model.utils.DomainUtils;
import me.becycled.backend.model.utils.EmailUtils;
import me.becycled.backend.service.AccessService;
import me.becycled.backend.service.email.BodyType;
import me.becycled.backend.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author I1yi4
 */
@RestController
@RequestMapping("/user-accounts")
@Api(description = "Авторизационные данные пользователей")
public class UserAccountController {

    private final DaoFactory daoFactory;
    private final AccessService accessService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public UserAccountController(final DaoFactory daoFactory,
                                 final AccessService accessService,
                                 final PasswordEncoder passwordEncoder,
                                 final EmailService emailService) {
        this.daoFactory = daoFactory;
        this.accessService = accessService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @RequestMapping(value = "/password/reset", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Обновить пароль текущего пользователя")
    public ResponseEntity<Void> resetPassword() {

        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        final UserAccount userAccount = daoFactory.getUserAccountDao().getByUserId(curUser.getId());

        final String newPassword = DomainUtils.generateUserPassword();
        userAccount.setPassword(passwordEncoder.encode(newPassword));
        daoFactory.getUserAccountDao().update(userAccount);

        emailService.send(curUser.getEmail(),
            EmailUtils.buildResetPasswordTopic(),
            EmailUtils.buildResetPasswordBody(curUser.getFirstName(), curUser.getLastName(), newPassword),
            BodyType.HTML);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/password", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Обновить пароль текущего пользователя")
    public ResponseEntity<Void> updatePassword(
        @ApiParam("Новый пароль пользователя") @RequestBody final String password) {

        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        final UserAccount userAccount = daoFactory.getUserAccountDao().getByUserId(curUser.getId());
        userAccount.setPassword(passwordEncoder.encode(password));
        daoFactory.getUserAccountDao().update(userAccount);
        return ResponseEntity.ok().build();
    }
}
