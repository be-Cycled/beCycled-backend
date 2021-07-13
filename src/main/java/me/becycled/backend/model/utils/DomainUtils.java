package me.becycled.backend.model.utils;

import java.util.UUID;

/**
 * @author I1yi4
 */
@SuppressWarnings("WhitespaceAround")
public enum DomainUtils {;

    public static String generateUUIDv4() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
