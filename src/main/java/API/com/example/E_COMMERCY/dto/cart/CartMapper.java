package API.com.example.E_COMMERCY.dto.cart;


import API.com.example.E_COMMERCY.dto.cart.request.AddCartItemRequestDto;
import API.com.example.E_COMMERCY.dto.cartItem.CartItemMapper;
import API.com.example.E_COMMERCY.model.Cart;
import API.com.example.E_COMMERCY.model.CartItem;
import API.com.example.E_COMMERCY.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartMapper {

    @Autowired
    private CartItemMapper cartItemMapper;
    @Autowired
    private @Lazy UserService userService;

    public CartResponseDto toDto(Cart cart){
        for (CartItem cartItem : cart.getCartItems()){
            System.out.println("CartItem quantity: " + cartItem.getQuantity());
            System.out.println("CartItem id: " + cartItem.getId());
            System.out.println("cartItemProductID : "+ cartItem.getProduct().getId());
        }
        System.out.println("------------------------------");
        return CartResponseDto.builder()
                .id((long) cart.getId())
                .cartItemResponseDtoSet(cart.getCartItems().stream().map(cartItemMapper::toDto)
                                       .collect(Collectors.toSet()))
                .build();
    }

    public Cart toEntity(CartResponseDto cartResponseDto){
        return Cart.builder()
                .cartItems(cartResponseDto.getCartItemResponseDtoSet()
                        .stream().map(cartItemMapper::toEntity).collect(Collectors.toSet()))
                .build();
    }

}
