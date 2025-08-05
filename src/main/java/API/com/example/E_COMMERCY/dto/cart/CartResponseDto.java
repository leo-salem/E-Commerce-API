package API.com.example.E_COMMERCY.dto.cart;

import API.com.example.E_COMMERCY.dto.cartItem.CartItemResponseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponseDto {


    private int id;


    private Set<CartItemResponseDto> CartItemResponseDtoSet;
}
