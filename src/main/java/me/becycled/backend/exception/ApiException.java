package me.becycled.backend.exception;

/**
 * @author I1yi4
 */
abstract class ApiException extends RuntimeException {

    private final String message;

    protected ApiException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
