package API.com.example.E_COMMERCY.exception;

public class UsernameAlreadyExistException extends Exception{
    public UsernameAlreadyExistException(){

    }
    public UsernameAlreadyExistException(String message){
        super(message);
    }
}
