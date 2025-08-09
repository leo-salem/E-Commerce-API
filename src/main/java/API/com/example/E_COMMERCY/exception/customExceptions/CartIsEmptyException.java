package API.com.example.E_COMMERCY.exception.customExceptions;

public class CartIsEmptyException extends Exception{
    public CartIsEmptyException() {
    }

    public CartIsEmptyException(String message) {
        super(message);
    }
}
