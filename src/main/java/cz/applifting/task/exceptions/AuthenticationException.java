package cz.applifting.task.exceptions;

/**
 * Indicates that a resource was not found.
 */
public class AuthenticationException extends AppliftingException{

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static AuthenticationException create() {
        return new AuthenticationException("User is not authenticated. Missing correct Authentication header.");
    }
}
