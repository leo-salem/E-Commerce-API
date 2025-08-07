package API.com.example.E_COMMERCY.dto.user;

import API.com.example.E_COMMERCY.dto.order.OrderResponseDto;
import API.com.example.E_COMMERCY.enums.Role;

import API.com.example.E_COMMERCY.model.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;

    private String username;

    private Role hasRole;

    private String LastName;

    private String FirstName;

    private String adress;

    private Set<OrderResponseDto> orderResponseDtoSet;

    private Cart cart;



}
