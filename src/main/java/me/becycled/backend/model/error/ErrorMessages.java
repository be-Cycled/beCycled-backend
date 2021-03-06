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

    public static <T> String onlyOwnerCanCreateEntity(final Class<T> clazz) {
        return String.format("%s can be created by owner only", firstLetterToUpperCase(camelCaseToNormalText(clazz.getSimpleName())));
    }

    public static <T> String onlyOwnerCanUpdateEntity(final Class<T> clazz) {
        return String.format("%s can be updated by owner only", firstLetterToUpperCase(camelCaseToNormalText(clazz.getSimpleName())));
    }

    public static <T> String onlyOwnerCanDeleteEntity(final Class<T> clazz) {
        return String.format("%s can be deleted by owner only", firstLetterToUpperCase(camelCaseToNormalText(clazz.getSimpleName())));
    }

    public static <T> String onlyOwnerCanReadEntity(final Class<T> clazz) {
        return String.format("Only owner can read %s", firstLetterToUpperCase(camelCaseToNormalText(clazz.getSimpleName())));
    }

    public static String userAlreadyJoin() {
        return "Current user is already joined";
    }

    public static String userNotJoin() {
        return "Current user is not joined";
    }

    public static String loginAlreadyExist() {
        return "Login is already using";
    }

    public static String emailAlreadyExist() {
        return "Email is already using";
    }

    public static <T> String alreadyExist(final Class<T> clazz) {
        return String.format("%s is already exist", firstLetterToUpperCase(camelCaseToNormalText(clazz.getSimpleName())));
    }

    public static String cannotFindImageExtension() {
        return "Not found image extension";
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
