package me.becycled.backend.exception;

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
