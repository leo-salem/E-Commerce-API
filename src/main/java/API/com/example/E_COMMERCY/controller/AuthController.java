package API.com.example.E_COMMERCY.controller;


import API.com.example.E_COMMERCY.dto.AuthenticationResponseDto;
import API.com.example.E_COMMERCY.dto.user.request.LoginRequestDto;
import API.com.example.E_COMMERCY.dto.user.request.RefreshRequestDto;
import API.com.example.E_COMMERCY.dto.user.request.RegisterRequestDto;
import API.com.example.E_COMMERCY.exception.customExceptions.InvalidTokenException;
import API.com.example.E_COMMERCY.exception.customExceptions.UsernameAlreadyExistException;
import API.com.example.E_COMMERCY.service.authentication.AuthenticationService;
import API.com.example.E_COMMERCY.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Authentication")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> Register(@Valid @RequestBody RegisterRequestDto registerRequestDto) throws UsernameAlreadyExistException {
        authenticationService.Register(registerRequestDto);
        return ResponseEntity.ok("register successfully done now plz login");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> Login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        AuthenticationResponseDto authenticationResponseDto=authenticationService.Login(loginRequestDto);
        return ResponseEntity.ok().body(authenticationResponseDto);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> Logout() {
        authenticationService.Logout(userService.getCurrentUsername());
        return ResponseEntity.ok("Logout successfully done");
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> RefreshToken(@Valid @RequestBody RefreshRequestDto refreshToken) throws InvalidTokenException {
        AuthenticationResponseDto authenticationResponse= authenticationService.refreshToken(refreshToken.getRefresh());
        return ResponseEntity.ok().body(authenticationResponse);
    }

}
