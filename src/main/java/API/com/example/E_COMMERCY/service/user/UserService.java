package API.com.example.E_COMMERCY.service.user;

import API.com.example.E_COMMERCY.dto.user.UserMapper;
import API.com.example.E_COMMERCY.dto.user.request.NameRequestDto;
import API.com.example.E_COMMERCY.dto.user.request.ChangePasswordRequestDto;
import API.com.example.E_COMMERCY.dto.user.request.DeleteUserRequestDto;
import API.com.example.E_COMMERCY.dto.user.UserResponseDto;
import API.com.example.E_COMMERCY.exception.customExceptions.InvalidPasswordException;
import API.com.example.E_COMMERCY.exception.customExceptions.UserNotFoundException;
import API.com.example.E_COMMERCY.model.User;
import API.com.example.E_COMMERCY.repository.UserRepository;
import API.com.example.E_COMMERCY.security.JwtUtils;
import API.com.example.E_COMMERCY.service.deletion.DeletionService;
import API.com.example.E_COMMERCY.service.token.TokenService;
import API.com.example.E_COMMERCY.service.tokenBlacklist.TokenBlacklistService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserInterface{


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final TokenService tokenService;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserDetailsManager userDetailsManager;
    private final DeletionService deletionService;

    @Autowired
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper,
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            TokenService tokenService,
            TokenBlacklistService tokenBlacklistService,
            @Qualifier("userDetailsManager") UserDetailsManager userDetailsManager, DeletionService deletionService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.tokenService = tokenService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.userDetailsManager = userDetailsManager;
        this.deletionService = deletionService;
    }

    @Override
    public UserResponseDto displayUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No user found with id: " + id));
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto displayUserByUsername(String Username) {
        User user =userRepository.findByUsername(Username)
                .orElseThrow(() -> new UserNotFoundException("No user found with username: " + Username));
        return userMapper.toDto(user);
    }

    @Override
    public String getCurrentUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    @Override
    public User getCurrentUser(String Username) {
        return userRepository.findByUsername(Username)
                .orElseThrow(() -> new UserNotFoundException("No user found with username: " + Username));
    }

    @Override
    public void ChangePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        Optional<User> optionalUser = Optional.ofNullable(getCurrentUser(getCurrentUsername()));
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        User user = optionalUser.get();
        if (passwordEncoder.matches(changePasswordRequestDto.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordRequestDto.getNewPassword()));
            userRepository.save(user);
        }
        else{
            throw new InvalidPasswordException();
        }
    }

    @Override
    public void ChangeName(NameRequestDto changeNameRequestDto) {
        Optional<User> optionalUser = Optional.ofNullable(getCurrentUser(getCurrentUsername()));
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        User user = optionalUser.get();
        user.setFirstName(changeNameRequestDto.getFirstName());
        user.setLastName(changeNameRequestDto.getLastName());
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void DeleteCurrentUser(DeleteUserRequestDto deleteUserRequestDto) {
        User user=getCurrentUser(getCurrentUsername());


        System.out.println("the current user password is "+user.getPassword());
        System.out.println("the deleted user password is "+deleteUserRequestDto.getPassword());
        System.out.println("the mastch is "+passwordEncoder.matches(deleteUserRequestDto.getConfirmPassword(), user.getPassword()) );


        if (!deleteUserRequestDto.getPassword().equals(deleteUserRequestDto.getPassword())) {
            throw new InvalidPasswordException("Password and ConfirmPassword do not match");
        }
        if (!passwordEncoder.matches(deleteUserRequestDto.getConfirmPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Password is wrong");
        }
        String username = user.getUsername();
        String AccessToken = tokenService.getAccessToken(username);
        String RefreshToken = tokenService.getRefreshToken(username);
        long accessRemaining = jwtUtils.getRemainingExpiration(AccessToken);
        long refreshRemaining = jwtUtils.getRemainingExpiration(RefreshToken);

        tokenBlacklistService.blacklistAccessToken(AccessToken, accessRemaining);
        tokenBlacklistService.blacklistRefreshToken(RefreshToken, refreshRemaining);

        tokenService.deleteAccessToken(username);
        tokenService.deleteRefreshToken(username);

        // فك الارتباط مع الطلبات
        if (user.getOrders() != null) {
            user.getOrders().forEach(deletionService::deleteOrder);
            user.getOrders().clear();
        }

        // حذف الكارت
        if (user.getCart() != null) {
            deletionService.deleteCart(user.getCart());
        }

        // حذف الـ Categories (لو Admin)
        if (user.getCategories() != null) {
            user.getCategories().forEach(deletionService::deleteCategory);
            user.getCategories().clear();
        }

        // فك الارتباط مع Products الخاصة بالمستخدم (لو USER)
        if (user.getProducts() != null) {
            user.getProducts().forEach(deletionService::deleteProduct);
            user.getProducts().clear();
        }

        userRepository.delete(user);
    }

}
