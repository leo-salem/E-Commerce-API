package API.com.example.E_COMMERCY.exception;

public class CartIsEmptyException extends Exception{
    public CartIsEmptyException() {
    }

    public CartIsEmptyException(String message) {
        super(message);
    }
}
