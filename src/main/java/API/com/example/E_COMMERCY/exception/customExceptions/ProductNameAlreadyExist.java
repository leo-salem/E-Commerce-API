package API.com.example.E_COMMERCY.exception.customExceptions;

public class ProductNameAlreadyExist extends Exception{
    public ProductNameAlreadyExist() {}
    public ProductNameAlreadyExist(String message) {
        super(message);
    }
}
