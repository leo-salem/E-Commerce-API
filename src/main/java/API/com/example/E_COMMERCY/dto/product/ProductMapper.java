package API.com.example.E_COMMERCY.dto.product;


import API.com.example.E_COMMERCY.dto.category.CategoryMapper;
import API.com.example.E_COMMERCY.dto.product.request.NewProductRequestDto;
import API.com.example.E_COMMERCY.dto.user.UserMapper;
import API.com.example.E_COMMERCY.model.Product;
import API.com.example.E_COMMERCY.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductMapper {

    private final @Lazy CategoryMapper categoryMapper;

    private final UserMapper userMapper;

    private final UserService userService;


    public ProductResponseDto toDto(Product product) {
        return ProductResponseDto.builder()
                .name(product.getName())
                .id(product.getId())
                .categoryResponseDto(categoryMapper.toDto(product.getCategory()))
                .price(product.getPrice())
                .description(product.getDescription())
                .userResponseDto(userMapper.toDto(product.getUser()))
                .build();
    }
    public Product toEntity(NewProductRequestDto newProductRequestDto) {
        return Product.builder()
                .name(newProductRequestDto.getName())
                .price(newProductRequestDto.getPrice())
                .category(categoryMapper.toEntity(newProductRequestDto.getCategoryResponseDto()))
                .description(newProductRequestDto.getDescription())
                .user(userService.getCurrentUser(userService.getCurrentUsername()))
                .build();
    }

    public Product toEntity(ProductResponseDto dto) {
        return Product.builder()
                .id(Math.toIntExact(dto.getId()))
                .name(dto.getName())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .category(categoryMapper.toEntity(dto.getCategoryResponseDto()))
                .build();
    }



}
