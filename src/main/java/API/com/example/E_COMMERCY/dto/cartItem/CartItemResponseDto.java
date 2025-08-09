package API.com.example.E_COMMERCY.dto.cartItem;

import API.com.example.E_COMMERCY.dto.product.ProductResponseDto;
import API.com.example.E_COMMERCY.dto.slimDTOs.ProductResponseSlimDto;
import API.com.example.E_COMMERCY.model.Product;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponseDto {


    private Long id;

    private int quantity;

    private ProductResponseSlimDto productResponseDto;
}
