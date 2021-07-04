package me.becycled.backend.model.error;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author I1yi4
 */
@SuppressWarnings("WhitespaceAround")
public enum ErrorMessages {;

    public static <T> String notFound(final Class<T> clazz) {
        return String.format("%s is not found", firstLetterToUpperCase(camelCaseToNormalText(clazz.getSimpleName())));
    }

    public static String differentIdentifierInPathAndBody() {
        return "Different identifiers in request path and body";
    }

    public static String authError() {
        return "Auth error";
    }

    public static <T> String onlyOwnerCanUpdateEntity(final Class<T> clazz) {
        return String.format("%s can be updated by owner only", firstLetterToUpperCase(camelCaseToNormalText(clazz.getSimpleName())));
    }

    public static String userAlreadyJoin() {
        return "Current user is already joined";
    }

    public static String userNotJoin() {
        return "Current user is not joined";
    }

    public static <T> String alreadyExist(final Class<T> clazz) {
        return String.format("%s is already exist", firstLetterToUpperCase(camelCaseToNormalText(clazz.getSimpleName())));
    }

    private static <T> String camelCaseToNormalText(final String text) {
        final List<String> strings = List.of(text.split("(?=[A-Z])")).stream()
            .map(String::toLowerCase)
            .collect(Collectors.toList());
        return String.join(" ", strings);
    }

    private static String firstLetterToUpperCase(final String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
