package cz.applifting.task.exceptions;

public class NullParameterException extends AppliftingException{
    public NullParameterException(String message) {
        super(message);
    }

    public NullParameterException(String message, Throwable cause) {
        super(message, cause);
    }


    public static void checkNullThrowNullParameter(Object entity, String entityName){
        if (entity == null){
            throw new NullParameterException("Entity attribute " + entityName + " musn't be null");
        }
    }
}
