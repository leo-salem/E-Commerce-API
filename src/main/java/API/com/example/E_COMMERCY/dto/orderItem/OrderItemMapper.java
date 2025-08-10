package API.com.example.E_COMMERCY.dto.orderItem;

import API.com.example.E_COMMERCY.dto.product.ProductMapper;
import API.com.example.E_COMMERCY.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class OrderItemMapper {


    private final @Lazy ProductMapper  productMapper;

    public OrderItemResponseDto toDto(OrderItem orderItem){
        return OrderItemResponseDto.builder()
                .productResponseDto(productMapper.toDto(orderItem.getProduct()))
                .id((long) orderItem.getId())
                .Quantity(orderItem.getQuantity())
                .build();
    }
}
