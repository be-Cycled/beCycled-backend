package me.becycled.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.becycled.backend.exception.AlreadyExistException;
import me.becycled.backend.exception.AuthException;
import me.becycled.backend.exception.NotFoundException;
import me.becycled.backend.exception.WrongRequestException;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.community.Community;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.entity.userprivacysetting.UserPrivacySetting;
import me.becycled.backend.model.error.ErrorMessages;
import me.becycled.backend.service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author I1yi4
 */
@RestController
@RequestMapping("/user-privacy-settings")
@Api(description = "Настройки приватности пользователя")
public class UserPrivacySettingController {

    private final DaoFactory daoFactory;
    private final AccessService accessService;

    @Autowired
    public UserPrivacySettingController(final DaoFactory daoFactory,
                               final AccessService accessService) {
        this.daoFactory = daoFactory;
        this.accessService = accessService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Создать настройки приватности пользователя")
    public ResponseEntity<UserPrivacySetting> create(
        @ApiParam("Настройки приватности пользователя") @RequestBody final UserPrivacySetting userPrivacySetting) {

        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        final UserPrivacySetting privacySetting = daoFactory.getUserPrivacySettingDao().getByUserId(userPrivacySetting.getUserId());
        if (privacySetting != null) {
            throw new AlreadyExistException(ErrorMessages.alreadyExist(UserPrivacySetting.class));
        }

        if (!curUser.getId().equals(userPrivacySetting.getUserId())) {
            throw new WrongRequestException(ErrorMessages.onlyOwnerCanCreateEntity(Community.class));
        }

        return ResponseEntity.ok(daoFactory.getUserPrivacySettingDao().create(userPrivacySetting));
    }

    @RequestMapping(value = "/{user-id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить настройки приватности пользователя")
    public ResponseEntity<UserPrivacySetting> getByUserId(
        @ApiParam("Идентификатор пользователя") @PathVariable("user-id") final int userId) {

        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        final UserPrivacySetting privacySetting = daoFactory.getUserPrivacySettingDao().getByUserId(userId);
        if (privacySetting == null) {
            throw new NotFoundException(ErrorMessages.notFound(UserPrivacySetting.class));
        }

        if (!curUser.getId().equals(userId)) {
            throw new WrongRequestException(ErrorMessages.onlyOwnerCanReadEntity(Community.class));
        }

        return ResponseEntity.ok(privacySetting);
    }

    @RequestMapping(value = "/{user-id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Обновить пользователя по его идентификатору")
    public ResponseEntity<UserPrivacySetting> update(
        @ApiParam("Идентификатор пользователя") @PathVariable("user-id") final int id,
        @ApiParam("Настройки приватности пользователя") @RequestBody final UserPrivacySetting entity) {

        if (id != entity.getUserId()) {
            throw new WrongRequestException(ErrorMessages.differentIdentifierInPathAndBody());
        }

        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        final UserPrivacySetting privacySetting = daoFactory.getUserPrivacySettingDao().getByUserId(entity.getUserId());
        if (privacySetting == null) {
            throw new NotFoundException(ErrorMessages.notFound(UserPrivacySetting.class));
        }

        if (!curUser.getId().equals(entity.getUserId())) {
            throw new WrongRequestException(ErrorMessages.onlyOwnerCanReadEntity(Community.class));
        }

        return ResponseEntity.ok(daoFactory.getUserPrivacySettingDao().update(entity));
    }
}
