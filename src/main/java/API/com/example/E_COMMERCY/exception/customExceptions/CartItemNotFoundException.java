package API.com.example.E_COMMERCY.exception.customExceptions;

public class CartItemNotFoundException extends Exception{
    public CartItemNotFoundException(){

    }

    public CartItemNotFoundException(String message) {
        super(message);
    }
}
