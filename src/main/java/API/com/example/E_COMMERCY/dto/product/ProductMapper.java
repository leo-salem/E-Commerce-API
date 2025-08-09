package API.com.example.E_COMMERCY.dto.product;


import API.com.example.E_COMMERCY.dto.product.request.NewProductRequestDto;
import API.com.example.E_COMMERCY.dto.slimDTOs.CategoryResponseSlimDto;
import API.com.example.E_COMMERCY.dto.user.UserMapper;
import API.com.example.E_COMMERCY.model.Product;
import API.com.example.E_COMMERCY.repository.CategoryRepository;
import API.com.example.E_COMMERCY.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class ProductMapper {

    private final @Lazy UserMapper userMapper;

    private final @Lazy UserService userService;

    private final @Lazy  CategoryRepository categoryRepository;


    public ProductResponseDto toDto(Product product) {
        return ProductResponseDto.builder()
                .name(product.getName())
                .id((long) product.getId())
                .categoryResponseDto(CategoryResponseSlimDto
                        .builder()
                        .name(product.getCategory().getName())
                        .id((long) product.getCategory().getId())
                        .build()
                )
                .price(product.getPrice())
                .description(product.getDescription())
                .userResponseDto(userMapper.toDto(product.getUser()))
                .build();
    }
    public Product toEntity(NewProductRequestDto newProductRequestDto) {
        return Product.builder()
                .name(newProductRequestDto.getName())
                .price(newProductRequestDto.getPrice())
                .category(categoryRepository.findById(newProductRequestDto.getCategoryResponseDto().getId())
                        .orElseThrow())
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
                .category(categoryRepository.findById(dto.getCategoryResponseDto().getId())
                        .orElseThrow())
                .build();
    }

}
