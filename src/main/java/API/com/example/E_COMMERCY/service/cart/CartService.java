package API.com.example.E_COMMERCY.service.cart;

import API.com.example.E_COMMERCY.dto.cart.CartMapper;
import API.com.example.E_COMMERCY.dto.cart.CartResponseDto;
import API.com.example.E_COMMERCY.dto.cart.request.AddCartItemRequestDto;
import API.com.example.E_COMMERCY.dto.cart.request.UpdateCartItemRequestDto;
import API.com.example.E_COMMERCY.dto.order.OrderMapper;
import API.com.example.E_COMMERCY.dto.order.OrderResponseDto;
import API.com.example.E_COMMERCY.exception.customExceptions.CartItemNotFoundException;
import API.com.example.E_COMMERCY.model.*;
import API.com.example.E_COMMERCY.repository.CartItemRepository;
import API.com.example.E_COMMERCY.repository.OrderRepository;
import API.com.example.E_COMMERCY.repository.ProductRepository;
import API.com.example.E_COMMERCY.repository.UserRepository;
import API.com.example.E_COMMERCY.service.deletion.DeletionService;
import API.com.example.E_COMMERCY.service.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class CartService implements CartInterface {


    private final UserService userService;
    private final CartMapper cartMapper;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final DeletionService deletionService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Autowired
    public CartService(UserService userService, CartMapper cartMapper,
                       CartItemRepository cartItemRepository,
                       ProductRepository productRepository,
                       DeletionService deletionService,
                       UserRepository userRepository, OrderRepository orderRepository,
                       OrderMapper orderMapper) {
        this.userService = userService;
        this.cartMapper = cartMapper;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.deletionService = deletionService;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }


    @Override
    public CartResponseDto displayCart() {
        System.out.println("===== DEBUG LOG =====");
        System.out.println("Current username: " + userService.getCurrentUsername());

        var currentUser = userService.getCurrentUser(userService.getCurrentUsername());
        System.out.println("Current user object: " + currentUser.getId());

        Cart cart = currentUser.getCart();
        System.out.println("Current cart object: " + cart.getId());
        return cartMapper.toDto(cart);
    }

    @Override
    public void AddCartItem(AddCartItemRequestDto cartItemRequestDto) throws CartItemNotFoundException {
        System.out.println("===== DEBUG LOG =====");
        System.out.println("Current username: " + userService.getCurrentUsername());

        var currentUser = userService.getCurrentUser(userService.getCurrentUsername());
        System.out.println("Current user object: " + currentUser.getId());

        Cart cart = currentUser.getCart();
        System.out.println("Current cart object: " + cart.getId());

        System.out.println("Product ID from request: " + cartItemRequestDto.getProductResponseDto().getId());

        var productOpt = productRepository.findById(cartItemRequestDto.getProductResponseDto().getId());
        if (productOpt.isEmpty()) {
            System.out.println(" Product not found in DB!");
        } else {
            System.out.println(" Product found: " + productOpt.get().getId());
        }

        Set<CartItem> cartItems = cart.getCartItems();
        for (CartItem cartItem : cartItems) {
            Long IdProduct = (long) cartItem.getProduct().getId();
            if (IdProduct.equals(cartItemRequestDto.getProductResponseDto().getId())) {
                System.out.println("Product already in cart → updating quantity...");
                UpdateCartItemRequestDto up = UpdateCartItemRequestDto.builder()
                        .quantity(cartItemRequestDto.getQuantity())
                        .Id((long) cartItem.getId())
                        .build();
                UpdateCartItem(up);
                return;
            }
        }

        CartItem cartItem = CartItem.builder()
                .Quantity(cartItemRequestDto.getQuantity())
                .product(productOpt.orElseThrow())
                .cart(cart)
                .build();
        cartItem.getCart().addCartItem(cartItem);
        cartItem.getProduct().getCartItems().add(cartItem);
        cartItemRepository.save(cartItem);

        System.out.println(" CartItem saved successfully.");
        System.out.println("======================");
    }


    @Override
    public void UpdateCartItem(UpdateCartItemRequestDto cartItemRequestDto) throws CartItemNotFoundException {
        CartItem cartItem = cartItemRepository.findById(cartItemRequestDto.getId())
                .orElseThrow(() -> new CartItemNotFoundException("there is no cart item you need"));
        cartItem.setQuantity(cartItemRequestDto.getQuantity()+cartItem.getQuantity());
        cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public OrderResponseDto convertCartToOrder() throws CartItemNotFoundException {
        User currentUser = userService.getCurrentUser(userService.getCurrentUsername());
        Cart cart = currentUser.getCart();

        System.out.println("Current User: " + currentUser);
        System.out.println("Cart: " + cart);
        if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new CartItemNotFoundException("Cart is empty or not found");
        }

        Order order = Order.builder()
                .date(new Date())
                .PaymentMethod("visa")
                .user(currentUser)
                .orderItems(new HashSet<>())
                .total(0.0)
                .build();

        double totalPrice = 0;

        // build order items and link both sides
        for (CartItem item : new HashSet<>(cart.getCartItems())) { // iterate on copy to be safe
            System.out.println("Processing CartItem: " + item);
            double itemTotal = item.getProduct().getPrice() * item.getQuantity();
            totalPrice += itemTotal;

            OrderItem orderItem = OrderItem.builder()
                    .product(item.getProduct())
                    .Quantity(item.getQuantity())   // لاحظ اسم الحقل عندك كبير Q
                    .order(order)
                    .build();

            // add to order using helper (sets both sides)
            order.addOrdertem(orderItem);

            // optional: keep product relation consistent
            if (item.getProduct().getOrderitems() == null) {
                item.getProduct().setOrderitems(new HashSet<>());
            }
            item.getProduct().getOrderitems().add(orderItem);

            System.out.println("OrderItem built (temp): " + orderItem);
        }

        order.setTotal(totalPrice);
        System.out.println("Order prepared, total = " + totalPrice);

        // **Important**: save the order (so DB will generate IDs for order and orderItems)
        Order savedOrder = orderRepository.save(order);
        // if you want to be 100% sure the INSERT happened now:
        // entityManager.flush();

        System.out.println("Saved order id = " + savedOrder.getId());
        savedOrder.getOrderItems().forEach(oi ->
                System.out.println("Saved orderItem id = " + oi.getId() + " for product " + oi.getProduct().getId())
        );

        ClearCart();
        System.out.println("Cart cleared");

        // map the SAVED order to DTO (this will have real ids)
        OrderResponseDto response = orderMapper.toDto(savedOrder);
        System.out.println("Returning DTO for order id = " + savedOrder.getId());
        return response;
    }


    @Override
    public void DeleteCartItem(Long Id) throws CartItemNotFoundException {
        CartItem cartItem = cartItemRepository.findById(Id)
                .orElseThrow(() -> new CartItemNotFoundException("there is no cart item you need"));
        deletionService.deleteCartItem(cartItem);
    }

    @Override
    public void ClearCart() throws CartItemNotFoundException {
        Cart cart = userService.getCurrentUser(userService.getCurrentUsername()).getCart();

        // نعمل نسخة مستقلة من العناصر
        Set<CartItem> itemsCopy = new HashSet<>(cart.getCartItems());

        for (CartItem cartItem : itemsCopy) {
            DeleteCartItem((long) cartItem.getId());
        }
    }


}
