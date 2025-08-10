package API.com.example.E_COMMERCY.dto.cart.request;

import API.com.example.E_COMMERCY.dto.slimDTOs.ProductResponseSlimDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddCartItemRequestDto {

    private int quantity;

    private ProductResponseSlimDto productResponseDto;

}
