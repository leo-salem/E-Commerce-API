package API.com.example.E_COMMERCY.dto.cartItem;


import API.com.example.E_COMMERCY.dto.product.ProductMapper;
import API.com.example.E_COMMERCY.dto.slimDTOs.ProductResponseSlimDto;
import API.com.example.E_COMMERCY.model.Cart;
import API.com.example.E_COMMERCY.model.CartItem;
import API.com.example.E_COMMERCY.repository.ProductRepository;
import API.com.example.E_COMMERCY.service.authentication.AuthenticationService;
import API.com.example.E_COMMERCY.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class CartItemMapper {

    private final @Lazy ProductMapper productMapper;
    private final @Lazy UserService userService;
    private final @Lazy ProductRepository productRepository;

    public CartItemResponseDto toDto(CartItem cartItem){
        return CartItemResponseDto.builder()
                .id((long) cartItem.getId())
                .quantity(cartItem.getQuantity())
                .productResponseDto(ProductResponseSlimDto.builder()
                        .name(cartItem.getProduct().getName())
                        .price(cartItem.getProduct().getPrice())
                        .id((long) cartItem.getProduct().getId())
                        .build())
                .build();
    }

    public CartItem toEntity(CartItemResponseDto cartItemResponseDto){
        return CartItem.builder()
                .Quantity(cartItemResponseDto.getQuantity())
                .product(productRepository.findById(cartItemResponseDto.getProductResponseDto().getId()).orElseThrow())
                .cart(userService.getCurrentUser(userService.getCurrentUsername()).getCart())
                .build();
    }

}
