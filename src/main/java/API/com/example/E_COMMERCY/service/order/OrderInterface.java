package API.com.example.E_COMMERCY.service.order;

import API.com.example.E_COMMERCY.dto.order.OrderResponseDto;
import API.com.example.E_COMMERCY.exception.CartIsEmptyException;
import API.com.example.E_COMMERCY.exception.OrderNotFoundException;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface OrderInterface {
    OrderResponseDto DisplayOrderById(Long id) throws OrderNotFoundException;
    List<OrderResponseDto> DisplayCurrentUserOrders();
    void MakeOrder() throws CartIsEmptyException;
    void DeleteOrderById(Long id) throws OrderNotFoundException, AccessDeniedException;
}
