package API.com.example.E_COMMERCY.dto.cart.request;


import API.com.example.E_COMMERCY.dto.cartItem.CartItemResponseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteCartItemRequestDto {

    @NotBlank
    public CartItemResponseDto cartItemResponseDto;

}
