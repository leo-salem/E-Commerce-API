package API.com.example.E_COMMERCY.exception.customExceptions;

public class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException(){}
    public CategoryNotFoundException(String message){
        super(message);
    }

    public CategoryNotFoundException(long id) {
    }
}
