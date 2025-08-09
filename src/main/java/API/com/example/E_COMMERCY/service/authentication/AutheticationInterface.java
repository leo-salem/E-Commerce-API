package API.com.example.E_COMMERCY.service.authentication;

import API.com.example.E_COMMERCY.dto.AuthenticationResponseDto;
import API.com.example.E_COMMERCY.dto.user.request.LoginRequestDto;
import API.com.example.E_COMMERCY.dto.user.request.RegisterRequestDto;
import API.com.example.E_COMMERCY.exception.customExceptions.InvalidTokenException;
import API.com.example.E_COMMERCY.exception.customExceptions.UsernameAlreadyExistException;

public interface AutheticationInterface {
    public void Register(RegisterRequestDto registerRequestDto) throws UsernameAlreadyExistException;
    public AuthenticationResponseDto Login(LoginRequestDto loginRequestDto);
    public void Logout(String Username);
    public AuthenticationResponseDto refreshToken(String refreshToken) throws InvalidTokenException;
}
