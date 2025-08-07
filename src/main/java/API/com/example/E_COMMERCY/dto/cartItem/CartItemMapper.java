package API.com.example.E_COMMERCY.dto.cartItem;


import API.com.example.E_COMMERCY.dto.product.ProductMapper;
import API.com.example.E_COMMERCY.model.Cart;
import API.com.example.E_COMMERCY.model.CartItem;
import API.com.example.E_COMMERCY.service.authentication.AuthenticationService;
import API.com.example.E_COMMERCY.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CartItemMapper {

    private final ProductMapper productMapper;
    private final UserService userService;

    public CartItemResponseDto toDto(CartItem cartItem){
        return CartItemResponseDto.builder()
                .id(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .productResponseDto(productMapper.toDto(cartItem.getProduct()))
                .build();
    }

    public CartItem toEntity(CartItemResponseDto cartItemResponseDto){
        return CartItem.builder()
                .Quantity(cartItemResponseDto.getQuantity())
                .product(productMapper.toEntity(cartItemResponseDto.getProductResponseDto()))
                .cart(userService.getCurrentUser(userService.getCurrentUsername()).getCart())
                .build();
    }

}
