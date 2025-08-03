package API.com.example.E_COMMERCY.dto.user.response;

import API.com.example.E_COMMERCY.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private int id;

    private String username;

    private Role hasRole;

    private String LastName;

    private String FirstName;

    private String adress;

}
