package API.com.example.E_COMMERCY.service.authentication;

import API.com.example.E_COMMERCY.dto.AuthenticationResponseDto;
import API.com.example.E_COMMERCY.dto.user.UserMapper;
import API.com.example.E_COMMERCY.dto.user.request.LoginRequestDto;
import API.com.example.E_COMMERCY.dto.user.request.RegisterRequestDto;
import API.com.example.E_COMMERCY.exception.customExceptions.InvalidPasswordException;
import API.com.example.E_COMMERCY.exception.customExceptions.InvalidTokenException;
import API.com.example.E_COMMERCY.exception.customExceptions.UserNotFoundException;
import API.com.example.E_COMMERCY.exception.customExceptions.UsernameAlreadyExistException;
import API.com.example.E_COMMERCY.model.User;
import API.com.example.E_COMMERCY.repository.UserRepository;
import API.com.example.E_COMMERCY.security.JwtUtils;
import API.com.example.E_COMMERCY.service.token.TokenService;
import API.com.example.E_COMMERCY.service.tokenBlacklist.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements AutheticationInterface{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final TokenService tokenService;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserDetailsManager userDetailsManager;

    @Autowired
    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper,
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            TokenService tokenService,
            TokenBlacklistService tokenBlacklistService,
            @Qualifier("userDetailsManager") UserDetailsManager userDetailsManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.tokenService = tokenService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.userDetailsManager = userDetailsManager;
    }

    @Override
    public void Register(RegisterRequestDto registerRequestDto) throws UsernameAlreadyExistException {
        String username = registerRequestDto.getUsername();
        if (userRepository.findByUsername(username).isPresent()) {
            throw new AuthenticationServiceException("Username already exists",
                    new UsernameAlreadyExistException());
        }
        if (!registerRequestDto.getPassword().equals(registerRequestDto.getConfirmPassword())) {
            throw new AuthenticationServiceException("invalid password",new  InvalidPasswordException());
        }
        User user = userMapper.toEntity(registerRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("Encoded password at register: " + user.getPassword());
        userRepository.save(user);
    }

    @Override
    public AuthenticationResponseDto Login(LoginRequestDto loginRequestDto) {
        try {

            String username = loginRequestDto.getUsername();
            String password = loginRequestDto.getPassword();

            var userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                var userEntity = userOpt.get();
                System.out.println("Raw password entered: " + password);
                System.out.println("Encoded password in DB: " + userEntity.getPassword());
                System.out.println("Matches? " + passwordEncoder.matches(password, userEntity.getPassword()));
            } else {
                System.out.println("User not found in DB");
            }


            UsernamePasswordAuthenticationToken tok = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(tok);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            System.out.println("Password loaded from DB: " + userDetails.getPassword());


            String AccessToken = jwtUtils.generateAccessToken(userDetails);
            String RefreshToken = jwtUtils.generateRefreshToken(userDetails);

            tokenService.saveAccessToken(username,AccessToken, jwtUtils.getRemainingExpiration(AccessToken));
            tokenService.saveRefreshToken(username,RefreshToken, jwtUtils.getRemainingExpiration(RefreshToken));

            return AuthenticationResponseDto.builder()
                    .AcessToken(AccessToken)
                    .RefreshToken(RefreshToken)
                    .build();
        }
        catch (BadCredentialsException e) {
            throw new AuthenticationServiceException("invalid password",new  InvalidPasswordException());
        }
        catch (UsernameNotFoundException e) {
            throw new  AuthenticationServiceException("Username not found",new UserNotFoundException());
        }
    }

    @Override
    public void Logout(String username) {
        String AccessToken = tokenService.getAccessToken(username);
        String RefreshToken = tokenService.getRefreshToken(username);
        long accessRemaining = jwtUtils.getRemainingExpiration(AccessToken);
        long refreshRemaining = jwtUtils.getRemainingExpiration(RefreshToken);

        tokenBlacklistService.blacklistAccessToken(AccessToken, accessRemaining);
        tokenBlacklistService.blacklistRefreshToken(RefreshToken, refreshRemaining);

        tokenService.deleteAccessToken(username);
        tokenService.deleteRefreshToken(username);
    }

    @Override
    public AuthenticationResponseDto refreshToken(String refreshToken) throws InvalidTokenException {

        if (!jwtUtils.validateJwtToken(refreshToken) || !jwtUtils.isRefreshToken(refreshToken)) {
              throw new AuthenticationServiceException("Token is invalid",new InvalidTokenException());
        }
        if (tokenBlacklistService.isBlacklisted(refreshToken)) {
            throw new AuthenticationServiceException("Token is blacklisted",new InvalidTokenException());
        }
        String username = jwtUtils.getUsernameFromJwtToken(refreshToken);
        String storedRefreshToken = tokenService.getRefreshToken(username);
        System.out.println("RefreshToken from request: " + refreshToken);
        System.out.println("RefreshToken from Redis: " + storedRefreshToken);
        System.out.println("Username from token: " + username);
        if (!tokenService.isRefreshTokenValid(username, refreshToken)) {
            throw new AuthenticationServiceException("Token does not match stored token",new InvalidTokenException());
        }

        UserDetails userDetails=userDetailsManager.loadUserByUsername(username);
        String newAccess = jwtUtils.generateAccessToken(userDetails);
        String newRefresh = jwtUtils.generateRefreshToken(userDetails);

        tokenService.saveAccessToken(username, newAccess, jwtUtils.getRemainingExpiration(newAccess));
        tokenService.saveRefreshToken(username, newRefresh, jwtUtils.getRemainingExpiration(newRefresh));


        tokenBlacklistService.blacklistRefreshToken(refreshToken, jwtUtils.getRemainingExpiration(refreshToken));

        return AuthenticationResponseDto.builder()
                .AcessToken(newAccess)
                .RefreshToken(newRefresh)
                .build();
    }
}
