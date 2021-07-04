package me.becycled.backend.exception;

/**
 * @author I1yi4
 */
public class AlreadyExistException extends ApiException {

    public AlreadyExistException(final String message) {
        super(message);
    }
}
