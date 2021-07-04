package me.becycled.backend.exception;

/**
 * @author I1yi4
 */
public class NotFoundException extends ApiException {

    public NotFoundException(final String message) {
        super(message);
    }
}
