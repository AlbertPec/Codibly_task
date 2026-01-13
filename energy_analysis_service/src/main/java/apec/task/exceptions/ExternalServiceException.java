package apec.task.exceptions;

public class ExternalServiceException extends RuntimeException{
    public ExternalServiceException(String msg) {
        super("External service error: " + msg);
    }
}
