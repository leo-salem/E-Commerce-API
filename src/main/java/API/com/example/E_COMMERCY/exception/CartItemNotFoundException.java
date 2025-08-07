package API.com.example.E_COMMERCY.exception;

public class CartItemNotFoundException extends Exception{
    public CartItemNotFoundException(){

    }

    public CartItemNotFoundException(String message) {
        super(message);
    }
}
