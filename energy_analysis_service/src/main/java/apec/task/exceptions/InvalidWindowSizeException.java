package apec.task.exceptions;

public class InvalidWindowSizeException extends RuntimeException {
    public InvalidWindowSizeException(int windowHours) {
        super("Window length " + windowHours + " is out of range. Valid range is 1 to 6 hours.");
    }
}
