package API.com.example.E_COMMERCY.dto.orderItem;

import API.com.example.E_COMMERCY.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponseDto {

    private int id;

    private int Quantity;

    private int Price;

    private Product product;
}
