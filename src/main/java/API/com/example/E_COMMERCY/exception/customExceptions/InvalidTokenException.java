package API.com.example.E_COMMERCY.exception.customExceptions;

public class InvalidTokenException extends Exception{
    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException() {
    }
}
