package API.com.example.E_COMMERCY.service.order;

import API.com.example.E_COMMERCY.dto.order.OrderMapper;
import API.com.example.E_COMMERCY.dto.order.OrderResponseDto;
import API.com.example.E_COMMERCY.exception.customExceptions.CartIsEmptyException;
import API.com.example.E_COMMERCY.exception.customExceptions.OrderNotFoundException;
import API.com.example.E_COMMERCY.model.Cart;
import API.com.example.E_COMMERCY.model.Order;
import API.com.example.E_COMMERCY.repository.OrderRepository;
import API.com.example.E_COMMERCY.service.cart.CartService;
import API.com.example.E_COMMERCY.service.deletion.DeletionService;
import API.com.example.E_COMMERCY.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService implements OrderInterface{


    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CartService cartService;
    private final DeletionService deletionService;

    @Autowired
    public OrderService(UserService userService, OrderRepository orderRepository, OrderMapper orderMapper, CartService cartService, DeletionService deletionService) {
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.cartService = cartService;
        this.deletionService = deletionService;
    }

    @Override
    public OrderResponseDto DisplayOrderById(Long id) throws OrderNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("There is no order with this id: + " + id));
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderResponseDto> DisplayCurrentUserOrders() {
        Set<Order> orders=userService.getCurrentUser(userService.getCurrentUsername()).getOrders();
        return orders.stream().map(orderMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void MakeOrder() throws CartIsEmptyException {
        Cart cart = userService.getCurrentUser(userService.getCurrentUsername()).getCart();
        if (cart.getCartItems().isEmpty()){
            throw new CartIsEmptyException("can't make order cause cart is empty");
        }
        cartService.convertCartToOrder();
    }

    @Override
    public void DeleteOrderById(Long id) throws OrderNotFoundException, AccessDeniedException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("There is no order with this id: + " + id));
        if (!order.getUser().equals(userService.getCurrentUser(userService.getCurrentUsername()))) {
            throw new AccessDeniedException("You cannot delete an order that does not belong to you.");
        }
        deletionService.deleteOrder(order);
    }
}
