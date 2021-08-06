package me.becycled.backend.model.utils;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.util.UUID;

/**
 * @author I1yi4
 */
@SuppressWarnings("WhitespaceAround")
public enum DomainUtils {;

    public static final String DEFAULT_USER_AVATAR_URL = "https://api.becycled.me/images/default_user_avatar.png";
    public static final String DEFAULT_COMMUNITY_AVATAR_URL = "https://api.becycled.me/images/default_community_avatar.png";

    private static final CharacterRule ALPHABETS = new CharacterRule(EnglishCharacterData.Alphabetical);
    private static final CharacterRule DIGITS = new CharacterRule(EnglishCharacterData.Digit);
    private static final PasswordGenerator PASSWORD_GENERATOR = new PasswordGenerator();

    public static String generateUUIDv4() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String generateUserPassword() {
        return PASSWORD_GENERATOR.generatePassword(16, ALPHABETS, DIGITS);
    }
}
