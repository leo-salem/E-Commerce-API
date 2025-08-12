package API.com.example.E_COMMERCY.dto.cartItem;


import API.com.example.E_COMMERCY.dto.product.ProductMapper;
import API.com.example.E_COMMERCY.dto.slimDTOs.ProductResponseSlimDto;
import API.com.example.E_COMMERCY.model.CartItem;
import API.com.example.E_COMMERCY.repository.ProductRepository;
import API.com.example.E_COMMERCY.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class CartItemMapper {

    @Autowired
    private final @Lazy ProductMapper productMapper;
    @Autowired
    private final @Lazy UserService userService;
    @Autowired
    private final @Lazy ProductRepository productRepository;

    public CartItemResponseDto toDto(CartItem cartItem){

        System.out.println("===========DEBUG LOG=========");
        System.out.println("CartItemMapper-> in it");
        if(cartItem.getProduct() == null){
            System.out.println("cartItem.getProduct == null");
            return null;
        }


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
