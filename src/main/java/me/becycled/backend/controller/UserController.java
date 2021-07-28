package me.becycled.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.becycled.backend.exception.AuthException;
import me.becycled.backend.exception.NotFoundException;
import me.becycled.backend.exception.WrongRequestException;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.error.ErrorMessages;
import me.becycled.backend.service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author I1yi4
 * @author binakot
 */
@RestController
@RequestMapping("/users")
@Api(description = "Пользователи")
public class UserController {

    private final DaoFactory daoFactory;
    private final AccessService accessService;

    @Autowired
    public UserController(final DaoFactory daoFactory,
                           final AccessService accessService) {
        this.daoFactory = daoFactory;
        this.accessService = accessService;
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить данные текущего пользователя")
    public ResponseEntity<User> getMe() {
        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }
        return ResponseEntity.ok(daoFactory.getUserDao().getByLogin(curUser.getLogin()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить пользователя по его идентификатору")
    public ResponseEntity<User> getById(
        @ApiParam("Идентификатор пользователя") @PathVariable("id") final int id) {

        final User user = daoFactory.getUserDao().getById(id);
        if (user == null) {
            throw new NotFoundException(ErrorMessages.notFound(User.class));
        }
        return ResponseEntity.ok(user);
    }

    @RequestMapping(value = "/multiple", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить пользователей по их идентификаторам")
    public ResponseEntity<List<User>> getByIds(
        @ApiParam("Идентификаторы пользователей") @RequestBody final List<Integer> ids) {

        return ResponseEntity.ok(daoFactory.getUserDao().getByIds(ids));
    }

    @RequestMapping(value = "/login/{login}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить пользователя по его логину")
    public ResponseEntity<User> getByLogin(
        @ApiParam("Логин пользователя") @PathVariable("login") final String login) {

        final User user = daoFactory.getUserDao().getByLogin(login);
        if (user == null) {
            throw new NotFoundException(ErrorMessages.notFound(User.class));
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
    public ResponseEntity<User> update(
        @ApiParam("Идентификатор пользователя") @PathVariable("id") final int id,
        @ApiParam("Данные пользователя") @RequestBody final User entity) {

        if (id != entity.getId()) {
            throw new WrongRequestException(ErrorMessages.differentIdentifierInPathAndBody());
        }

        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        if (!curUser.getId().equals(id)) {
            throw new WrongRequestException(ErrorMessages.onlyOwnerCanUpdateEntity(User.class));
        }

        return ResponseEntity.ok(daoFactory.getUserDao().update(entity));
    }
}
