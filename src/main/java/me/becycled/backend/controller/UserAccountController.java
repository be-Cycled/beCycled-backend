package me.becycled.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.entity.user.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserAccountController(final DaoFactory daoFactory,
                                 final PasswordEncoder passwordEncoder) {
        this.daoFactory = daoFactory;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = "/password", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Обновить пароль текущего пользователя")
    public ResponseEntity<?> updatePassword(
        @ApiParam("Новый пароль пользователя") @RequestBody final String password) {

        final User curUser = daoFactory.getUserDao().getByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (curUser == null) {
            return new ResponseEntity<>("Auth error", HttpStatus.UNAUTHORIZED);
        }

        final UserAccount userAccount = daoFactory.getUserAccountDao().getByUserId(curUser.getId());
        userAccount.setPassword(passwordEncoder.encode(password));

        return ResponseEntity.ok(daoFactory.getUserAccountDao().update(userAccount));
    }
}
