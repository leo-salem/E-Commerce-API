package API.com.example.E_COMMERCY.security;

import API.com.example.E_COMMERCY.exception.customExceptions.InvalidPasswordException;
import API.com.example.E_COMMERCY.exception.customExceptions.InvalidTokenException;
import API.com.example.E_COMMERCY.exception.customExceptions.UsernameAlreadyExistException;
import API.com.example.E_COMMERCY.exception.customExceptions.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        Throwable cause = authException.getCause() != null ? authException.getCause() : authException;

        String message;
        if (cause instanceof InvalidPasswordException) {
            message = "Invalid password provided";
        } else if (cause instanceof InvalidTokenException) {
            message = "Invalid or expired token";
        } else if (cause instanceof UsernameAlreadyExistException) {
            message = "Username already exists";
        } else if (cause instanceof UserNotFoundException) {
            message = "User not found";
        } else {
            message = authException.getMessage() != null ? authException.getMessage() : "Unauthorized access";
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> json = new HashMap<>();
        json.put("message", message);
        json.put("error", "Unauthorized");
        json.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        json.put("path", request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), json);
    }
}
