package me.becycled.backend.controller;

import me.becycled.backend.dto.RegisterDto;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.User;
import me.becycled.backend.model.entity.UserAccount;
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
    public ResponseEntity<String> create(@RequestBody @Validated final RegisterDto entity) {
        final User userByLogin = daoFactory.getUserDao().getByLogin(entity.getLogin());
        if (userByLogin != null) {
            return ResponseEntity.badRequest().body("Login already using");
        }
        final User userByEmail = daoFactory.getUserDao().getByEmail(entity.getEmail());
        if (userByEmail != null) {
            return ResponseEntity.badRequest().body("Email already using");
        }

        // TODO transaction
        final User user = new User();
        user.setLogin(entity.getLogin());
        user.setEmail(entity.getEmail());
        final User createdUser = daoFactory.getUserDao().create(user);
        final UserAccount userAccount = new UserAccount();
        userAccount.setUserId(createdUser.getId());
        userAccount.setPassword(passwordEncoder.encode(entity.getPassword()));
        daoFactory.getUserAccountDao().create(userAccount);

        return ResponseEntity.ok().build();
    }
}
