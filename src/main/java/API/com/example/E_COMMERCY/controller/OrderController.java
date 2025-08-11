package API.com.example.E_COMMERCY.controller;


import API.com.example.E_COMMERCY.dto.order.OrderResponseDto;
import API.com.example.E_COMMERCY.exception.customExceptions.CartIsEmptyException;
import API.com.example.E_COMMERCY.exception.customExceptions.CartItemNotFoundException;
import API.com.example.E_COMMERCY.exception.customExceptions.OrderNotFoundException;
import API.com.example.E_COMMERCY.service.order.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/OrderSection")
public class OrderController {

    private final OrderService orderService;


    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("order/TheId/{Id}")
    public ResponseEntity<?> GetOrderById(@PathVariable @Valid Long Id) throws OrderNotFoundException {
        OrderResponseDto orderResponseDto = orderService.DisplayOrderById(Id);
        return ResponseEntity.ok().body(orderResponseDto);
    }

    @GetMapping("orders")
    public ResponseEntity<?> GetAllCurrentUserOrders(){
        List<OrderResponseDto> orderResponseDtos =orderService.DisplayCurrentUserOrders();
        return ResponseEntity.ok().body(orderResponseDtos);
    }

    @GetMapping("order/make")
    public ResponseEntity<?> MakeTheOrder() throws CartItemNotFoundException, CartIsEmptyException {
        OrderResponseDto orderResponseDto =orderService.MakeOrder();
        return ResponseEntity.ok().body(orderResponseDto);
    }

    @DeleteMapping("order/delete/{id}")
    public  ResponseEntity<?> DeleteOrderFromCurrentUser(@PathVariable @Valid Long id) throws OrderNotFoundException, AccessDeniedException {
        orderService.DeleteOrderById(id);
        return ResponseEntity.ok("order deleted successfully");
    }

}
