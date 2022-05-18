package cz.applifting.task.exceptions;

/**
 * Indicates that given URL is either not valid or not found
 */
public class BadUrlException extends AppliftingException{

    public BadUrlException(String message) {
        super(message);
    }

    public BadUrlException(String message, Throwable cause) {
        super(message, cause);
    }

    public static BadUrlException create(String url) {
        return new BadUrlException("URL " + url + "  is either not valid or was not found");
    }
}
