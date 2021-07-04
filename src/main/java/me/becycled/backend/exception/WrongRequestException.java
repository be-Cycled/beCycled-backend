package me.becycled.backend.exception;

/**
 * @author I1yi4
 */
public class WrongRequestException extends ApiException {

    public WrongRequestException(final String message) {
        super(message);
    }
}
