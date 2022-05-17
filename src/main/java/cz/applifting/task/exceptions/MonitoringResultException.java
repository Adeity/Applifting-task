package cz.applifting.task.exceptions;

/**
 * Indicates that monitoring result could not be retrieved
 */
public class MonitoringResultException extends AppliftingException{

    public MonitoringResultException(String message) {
        super(message);
    }

    public MonitoringResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public static MonitoringResultException create(String url, String owner) {
        return new MonitoringResultException(url + " identified by owner " + owner + " could not be monitored.");
    }
}
