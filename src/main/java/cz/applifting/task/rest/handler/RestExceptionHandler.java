package cz.applifting.task.rest.handler;

import cz.applifting.task.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Exception handlers for REST controllers.
 * <p>
 * The general pattern should be that unless an exception can be handled in a more appropriate place it bubbles up to a
 * REST controller which originally received the request. There, it is caught by this handler, logged and a reasonable
 * error message is returned to the user.
 */
@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    private static void logException(RuntimeException ex) {
        LOG.error("Exception caught:", ex);
    }

    private static ErrorInfo errorInfo(HttpServletRequest request, Throwable e) {
        return new ErrorInfo(e.getMessage(), request.getRequestURI());
    }

    /**
     * Gets thrown when desired object was not found
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> notFoundException(HttpServletRequest request, NotFoundException e) {
        logException(e);
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.NOT_FOUND);
    }

    /**
     * Gets thrown when user attempts to create MonitoredEndpoint with invalid URL
     */
    @ExceptionHandler(BadUrlException.class)
    public ResponseEntity<ErrorInfo> badUrlException(HttpServletRequest request, BadUrlException e) {
        logException(e);
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.BAD_REQUEST);
    }

    /**
     * Gets thrown when if a command to create MonitoringResult of an inactive MonitoredEndpoint
     */
    @ExceptionHandler(MonitoringResultException.class)
    public ResponseEntity<ErrorInfo> monitoringResultException(HttpServletRequest request, MonitoringResultException e) {
        logException(e);
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.BAD_REQUEST);
    }

    /**
     * Gets thrown when an user wants to create entity with not-null null attributes
     */
    @ExceptionHandler(NullParameterException.class)
    public ResponseEntity<ErrorInfo> nullParameterException(HttpServletRequest request, NullParameterException e) {
        logException(e);
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.BAD_REQUEST);
    }

    /**
     * Gets thrown when there is no authenticated user
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorInfo> authenticationException(HttpServletRequest request, AuthenticationException e) {
        logException(e);
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.UNAUTHORIZED);
    }
}
