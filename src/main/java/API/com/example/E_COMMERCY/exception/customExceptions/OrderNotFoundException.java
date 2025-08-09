package API.com.example.E_COMMERCY.exception.customExceptions;

public class OrderNotFoundException extends Exception{
    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException() {
    }
}
