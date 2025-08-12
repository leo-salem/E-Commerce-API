package API.com.example.E_COMMERCY.dto.orderItem;

import API.com.example.E_COMMERCY.dto.product.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponseDto {

    private Long id;

    private int Quantity;

    private ProductResponseDto productResponseDto;
}
