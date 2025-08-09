package API.com.example.E_COMMERCY.exception.customExceptions;

public class CategoryNameAlreadyExist extends Exception{
    public CategoryNameAlreadyExist() {
    }
    public CategoryNameAlreadyExist(String message) {
        super(message);
    }
}
