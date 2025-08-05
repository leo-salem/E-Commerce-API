package API.com.example.E_COMMERCY.service.authentication;

import API.com.example.E_COMMERCY.dto.AuthenticationResponseDto;
import API.com.example.E_COMMERCY.dto.user.UserMapper;
import API.com.example.E_COMMERCY.dto.user.request.LoginRequestDto;
import API.com.example.E_COMMERCY.dto.user.request.RegisterRequestDto;
import API.com.example.E_COMMERCY.exception.InvalidPasswordException;
import API.com.example.E_COMMERCY.exception.InvalidTokenException;
import API.com.example.E_COMMERCY.exception.UserNotFoundException;
import API.com.example.E_COMMERCY.exception.UsernameAlreadyExistException;
import API.com.example.E_COMMERCY.model.User;
import API.com.example.E_COMMERCY.repository.UserRepository;
import API.com.example.E_COMMERCY.security.JwtUtils;
import API.com.example.E_COMMERCY.service.token.TokenService;
import API.com.example.E_COMMERCY.service.tokenBlacklist.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
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
            throw new UsernameAlreadyExistException("Username " + username + " is already taken");
        }

        User user = userMapper.toEntity(registerRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public AuthenticationResponseDto Login(LoginRequestDto loginRequestDto) {
        try {

            String username = loginRequestDto.getUsername();
            String password = loginRequestDto.getPassword();

            UsernamePasswordAuthenticationToken tok = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(tok);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

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
            throw new InvalidPasswordException("Invalid password");
        }
        catch (UsernameNotFoundException e) {
            throw new UserNotFoundException("User not found");
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
            throw new InvalidTokenException("Invalid refresh token");
        }
        if (tokenBlacklistService.isBlacklisted(refreshToken)) {
            throw new InvalidTokenException("Token is blacklisted");
        }
        String username = jwtUtils.getUsernameFromJwtToken(refreshToken);
        if (!tokenService.isRefreshTokenValid(username, refreshToken)) {
            throw new InvalidTokenException("Token does not match stored token");
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
