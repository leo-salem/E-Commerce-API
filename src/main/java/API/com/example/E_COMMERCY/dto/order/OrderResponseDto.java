package API.com.example.E_COMMERCY.dto.order;

import API.com.example.E_COMMERCY.dto.orderItem.OrderItemResponseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {

    private Long id;


    private Date date;


    private double total;


    private String PaymentMethod;


    private Set<OrderItemResponseDto> orderItemResponseDtoSet;



}
