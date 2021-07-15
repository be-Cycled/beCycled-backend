package me.becycled.backend.model.utils;

import java.util.UUID;

/**
 * @author I1yi4
 */
@SuppressWarnings("WhitespaceAround")
public enum DomainUtils {;

    public static final String DEFAULT_USER_AVATAR_URL = "https://api.becycled.me/images/default_avatar.png";

    public static String generateUUIDv4() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
