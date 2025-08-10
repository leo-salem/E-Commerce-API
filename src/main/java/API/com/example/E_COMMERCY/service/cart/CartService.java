package API.com.example.E_COMMERCY.service.cart;

import API.com.example.E_COMMERCY.dto.cart.CartMapper;
import API.com.example.E_COMMERCY.dto.cart.CartResponseDto;
import API.com.example.E_COMMERCY.dto.cart.request.AddCartItemRequestDto;
import API.com.example.E_COMMERCY.dto.cart.request.UpdateCartItemRequestDto;
import API.com.example.E_COMMERCY.exception.customExceptions.CartItemNotFoundException;
import API.com.example.E_COMMERCY.model.Cart;
import API.com.example.E_COMMERCY.model.CartItem;
import API.com.example.E_COMMERCY.model.Order;
import API.com.example.E_COMMERCY.model.OrderItem;
import API.com.example.E_COMMERCY.repository.CartItemRepository;
import API.com.example.E_COMMERCY.repository.OrderRepository;
import API.com.example.E_COMMERCY.repository.ProductRepository;
import API.com.example.E_COMMERCY.service.deletion.DeletionService;
import API.com.example.E_COMMERCY.service.user.UserService;
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

    @Autowired
    public CartService(UserService userService, CartMapper cartMapper, CartItemRepository cartItemRepository, ProductRepository productRepository, DeletionService deletionService, OrderRepository orderRepository) {
        this.userService = userService;
        this.cartMapper = cartMapper;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
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
    public void convertCartToOrder() throws CartItemNotFoundException {
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
        ClearCart();
    }

    @Override
    public void DeleteCartItem(Long Id) throws CartItemNotFoundException {
        CartItem cartItem = cartItemRepository.findById(Id)
                .orElseThrow(() -> new CartItemNotFoundException("there is no cart item you need"));
        deletionService.deleteCartItem(cartItem);
    }

    @Override
    public void ClearCart() throws CartItemNotFoundException {
        Cart cart =userService.getCurrentUser(userService.getCurrentUsername()).getCart();
        for (CartItem cartItem : cart.getCartItems()){
            DeleteCartItem((long) cartItem.getId());
        }
    }

}
