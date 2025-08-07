package API.com.example.E_COMMERCY.service.cart;

import API.com.example.E_COMMERCY.dto.cart.CartMapper;
import API.com.example.E_COMMERCY.dto.cart.CartResponseDto;
import API.com.example.E_COMMERCY.dto.cart.request.AddCartItemRequestDto;
import API.com.example.E_COMMERCY.dto.cart.request.DeleteCartItemRequestDto;
import API.com.example.E_COMMERCY.dto.cart.request.UpdateCartItemRequestDto;
import API.com.example.E_COMMERCY.dto.cartItem.CartItemMapper;
import API.com.example.E_COMMERCY.dto.product.ProductMapper;
import API.com.example.E_COMMERCY.exception.CartItemNotFoundException;
import API.com.example.E_COMMERCY.model.Cart;
import API.com.example.E_COMMERCY.model.CartItem;
import API.com.example.E_COMMERCY.model.Order;
import API.com.example.E_COMMERCY.model.OrderItem;
import API.com.example.E_COMMERCY.repository.CartItemRepository;
import API.com.example.E_COMMERCY.repository.OrderRepository;
import API.com.example.E_COMMERCY.service.deletion.DeletionService;
import API.com.example.E_COMMERCY.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class CartService implements CartInterface {


    private final UserService userService;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final ProductMapper productMapper;
    private final DeletionService deletionService;
    private final OrderRepository orderRepository;

    @Autowired
    public CartService(UserService userService, CartMapper cartMapper, CartItemMapper cartItemMapper, CartItemRepository cartItemRepository, ProductMapper productMapper, DeletionService deletionService, OrderRepository orderRepository) {
        this.userService = userService;
        this.cartMapper = cartMapper;
        this.cartItemMapper = cartItemMapper;
        this.cartItemRepository = cartItemRepository;
        this.productMapper = productMapper;
        this.deletionService = deletionService;
        this.orderRepository = orderRepository;
    }


    @Override
    public CartResponseDto displayCart() {
        Cart cart = userService.getCurrentUser(userService.getCurrentUsername()).getCart();
        return cartMapper.toDto(cart);
    }

    @Override
    public void AddCartItem(AddCartItemRequestDto cartItemRequestDto) throws CartItemNotFoundException {
        Cart cart = userService.getCurrentUser(userService.getCurrentUsername()).getCart();

        if (cartItemRepository.findById(cartItemRequestDto.cartItemResponseDto.getId()).isPresent()) {
            UpdateCartItemRequestDto up =UpdateCartItemRequestDto.builder()
                    .quantity(cartItemRequestDto.getQuantity())
                    .cartItemResponseDto(cartItemRequestDto.getCartItemResponseDto())
                    .build();
             UpdateCartItem(up);
        }
        else{
            CartItem cartItem=CartItem.builder()
                    .Quantity(cartItemRequestDto.quantity)
                    .product(productMapper
                            .toEntity(cartItemRequestDto
                                    .cartItemResponseDto
                                    .getProductResponseDto()))
                    .cart(userService
                            .getCurrentUser(userService
                                    .getCurrentUsername())
                            .getCart())
                    .build();
            cartItem.getCart().addCartItem(cartItem);
            cartItem.getProduct().getCartItems().add(cartItem);
            cartItemRepository.save(cartItem);
        }

    }

    @Override
    public void UpdateCartItem(UpdateCartItemRequestDto cartItemRequestDto) throws CartItemNotFoundException {
        CartItem cartItem = cartItemRepository.findById(cartItemRequestDto.cartItemResponseDto.getId())
                .orElseThrow(() -> new CartItemNotFoundException("there is no cart item you need"));
        cartItem.setQuantity(cartItemRequestDto.getQuantity());
        cartItemRepository.save(cartItem);
    }

    @Override
    public void convertCartToOrder() {
        Cart cart =userService.getCurrentUser(userService.getCurrentUsername()).getCart();
        Order order = new Order();
        order.setDate(new Date());
        order.setPaymentMethod("visa");
        order.setUser(userService.getCurrentUser(userService.getCurrentUsername()));
        Set<OrderItem> orderItems = new HashSet<>();
        double totalPrice = 0;

        for (CartItem item : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setOrder(order);
            orderItem.getProduct().getOrderitems().add(orderItem);
            totalPrice +=  item.getProduct().getPrice()* item.getQuantity();
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        order.setTotal(totalPrice);
        order.setUser(userService.getCurrentUser(userService.getCurrentUsername()));
        order.getUser().getOrders().add(order);
        orderRepository.save(order);
    }

    @Override
    public void DeleteCartItem(DeleteCartItemRequestDto cartItemRequestDto) throws CartItemNotFoundException {
        CartItem cartItem = cartItemRepository.findById(cartItemRequestDto.cartItemResponseDto.getId())
                .orElseThrow(() -> new CartItemNotFoundException("there is no cart item you need"));
        deletionService.deleteCartItem(cartItem);
    }

    @Override
    public void ClearCart() {
        Cart cart =userService.getCurrentUser(userService.getCurrentUsername()).getCart();
        cart.getCartItems().forEach(deletionService::deleteCartItem);
    }

}
