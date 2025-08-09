package API.com.example.E_COMMERCY.exception.customExceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {

    }
    public UserNotFoundException(String message) {
        super(message);
    }

}
