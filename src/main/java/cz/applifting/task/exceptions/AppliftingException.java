package cz.applifting.task.exceptions;

/**
 * Base for all application-specific exceptions.
 */
public class AppliftingException extends RuntimeException {

    public AppliftingException() {
    }

    public AppliftingException(String message) {
        super(message);
    }

    public AppliftingException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppliftingException(Throwable cause) {
        super(cause);
    }
}
