package API.com.example.E_COMMERCY.dto.order;


import API.com.example.E_COMMERCY.dto.orderItem.OrderItemMapper;
import API.com.example.E_COMMERCY.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor

public class OrderMapper {

    private final @Lazy OrderItemMapper orderItemMapper;

    public OrderResponseDto toDto(Order order){

        return OrderResponseDto.builder()
                .id((long) order.getId())
                .date(order.getDate())
                .total(order.getTotal())
                .PaymentMethod(order.getPaymentMethod())
                .orderItemResponseDtoSet(order.getOrderItems()
                        .stream().map(orderItemMapper::toDto)
                        .collect(Collectors.toSet())
                )
                .build();
    }


}
