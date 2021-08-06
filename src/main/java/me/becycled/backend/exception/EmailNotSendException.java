package me.becycled.backend.exception;

/**
 * @author I1yi4
 */
public class EmailNotSendException extends RuntimeException {

    private final String message;

    public EmailNotSendException(final String message, final Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
