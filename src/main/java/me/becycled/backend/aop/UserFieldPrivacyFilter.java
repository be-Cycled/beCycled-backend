package me.becycled.backend.aop;

import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.entity.userprivacysetting.PrivacyRule;
import me.becycled.backend.model.entity.userprivacysetting.UserPrivacySetting;

import javax.el.MethodNotFoundException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author I1yi4
 */
public class UserFieldPrivacyFilter {

    private static final List<String> ALLOWABLE_USER_PRIVACY_SETTING_FIELDS = List.of("phone", "email");
    private static final PrivacyRule DEFAULT_RULE = PrivacyRule.OWNER_ONLY;

    private final DaoFactory daoFactory;

    public UserFieldPrivacyFilter(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public void filterNotAccessibleField(final Object object, final User curUser) {
        try {
            if (object instanceof Map) {
                final Map map = (Map) object;
                for (Object key : map.keySet()) {
                    filterNotAccessibleField(key, curUser);
                }
                for (Object value : map.values()) {
                    filterNotAccessibleField(value, curUser);
                }
            } else if (object instanceof Collection) {
                final Collection collection = (Collection) object;
                for (final Object value : collection) {
                    filterNotAccessibleField(value, curUser);
                }
            } else {
                if (!object.getClass().equals(User.class)) {
                    throw new IllegalArgumentException("Cannot accept user privacy setting for class = " + object.getClass());
                }
                filterNotAccessibleFieldForSingleObject((User) object, curUser);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void filterNotAccessibleFieldForSingleObject(final User user, final User curUser) {
        if (curUser != null && user.getId().equals(curUser.getId())) {
            return; // Свои собственные поля авторизованный пользователь видит всегда
        }
        final UserPrivacySetting userPrivacySetting = daoFactory.getUserPrivacySettingDao().getByUserId(user.getId());
        final Map<String, PrivacyRule> privacySettings = userPrivacySetting != null ? userPrivacySetting.getPrivacySettings() : Collections.emptyMap();
        for (final String userFieldName : ALLOWABLE_USER_PRIVACY_SETTING_FIELDS) {
            final PrivacyRule privacyRule = privacySettings.getOrDefault(userFieldName, DEFAULT_RULE);
            if (privacyRule == PrivacyRule.OWNER_ONLY) {
                callSetter(user, userFieldName, null);
            }
        }
    }

    private void callSetter(final Object object, final String fieldName, final Object newValue) {
        try {
            final Method fieldMethod = Stream.of(object.getClass().getDeclaredMethods(), object.getClass().getMethods())
                .flatMap(Arrays::stream)
                .filter(method -> method.getName().equals("set" + fieldName.toUpperCase().substring(0, 1) + fieldName.substring(1)))
                .findFirst()
                .orElseThrow(MethodNotFoundException::new);
            fieldMethod.invoke(object, newValue);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot call set for field with name " + fieldName + " on class " + object.getClass(), ex);
        }
    }
}
