package API.com.example.E_COMMERCY.dto.user;

import API.com.example.E_COMMERCY.dto.user.request.RegisterRequestDto;
import API.com.example.E_COMMERCY.dto.user.response.UserResponseDto;
import API.com.example.E_COMMERCY.enums.Role;
import API.com.example.E_COMMERCY.model.Cart;
import API.com.example.E_COMMERCY.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {

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
                .FirstName(userRequestDTO.getFirstName())
                .LastName(userRequestDTO.getLastName())
                .username(userRequestDTO.getUsername())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .hasRole(Role.USER)
                .adress(userRequestDTO.getAdress())
                .build();
    }
}
