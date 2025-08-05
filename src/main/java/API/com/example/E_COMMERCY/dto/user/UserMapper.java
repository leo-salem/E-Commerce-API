package API.com.example.E_COMMERCY.dto.user;

import API.com.example.E_COMMERCY.controller.Auth;
import API.com.example.E_COMMERCY.dto.user.request.RegisterRequestDto;
import API.com.example.E_COMMERCY.dto.user.response.UserResponseDto;
import API.com.example.E_COMMERCY.enums.Role;
import API.com.example.E_COMMERCY.model.Cart;
import API.com.example.E_COMMERCY.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .FirstName(user.getFirstName())
                .LastName(user.getLastName())
                .hasRole(user.getHasRole())
                .username(user.getUsername())
                .build();
    }

    public User toEntity(RegisterRequestDto userRequestDTO) {
        return User.builder()
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .username(userRequestDTO.getUsername())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .hasRole(Role.ROLE_USER)
                .cart(new Cart())
                .adress(userRequestDTO.getAdress())
                .build();
    }

}
