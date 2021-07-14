package me.becycled.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author I1yi4
 */
@RestController
@RequestMapping("/users")
@Api(description = "Пользователи")
public class UserController {

    private final DaoFactory daoFactory;

    @Autowired
    public UserController(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить данные текущего пользователя")
    public ResponseEntity<?> getMe() {
        final Object login = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (login == null) {
            return new ResponseEntity<>("User not auth", HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(daoFactory.getUserDao().getByLogin(login.toString()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить пользователя по его идентификатору")
    public ResponseEntity<?> getById(
        @ApiParam("Идентификатор пользователя") @PathVariable("id") final int id) {

        final User user = daoFactory.getUserDao().getById(id);
        if (user == null) {
            return new ResponseEntity<>("User is not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    @RequestMapping(value = "/login/{login}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить пользователя по его логину")
    public ResponseEntity<?> getByLogin(
        @ApiParam("Логин пользователя") @PathVariable("login") final String login) {

        final User user = daoFactory.getUserDao().getByLogin(login);
        if (user == null) {
            return new ResponseEntity<>("User is not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить всех пользователей")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(daoFactory.getUserDao().getAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Обновить пользователя по его идентификатору")
    public ResponseEntity<?> update(
        @ApiParam("Идентификатор пользователя") @PathVariable("id") final int id,
        @ApiParam("Данные пользователя") @RequestBody final User entity) {

        if (id != entity.getId()) {
            return new ResponseEntity<>("Different identifiers in request path and body", HttpStatus.BAD_REQUEST);
        }

        final User curUser = daoFactory.getUserDao().getByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (curUser == null) {
            return new ResponseEntity<>("Auth error", HttpStatus.UNAUTHORIZED);
        }

        if (!curUser.getId().equals(id)) {
            return new ResponseEntity<>("User can be updated by itself only", HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(daoFactory.getUserDao().update(entity));
    }
}
