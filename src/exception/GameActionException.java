package exception;

public class GameActionException extends Exception {
    public GameActionException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Warning: " + super.getMessage();
    }
}