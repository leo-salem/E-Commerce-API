package API.com.example.E_COMMERCY.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException (){

    }
    public InvalidPasswordException(String message) {
        super(message);
    }
}
