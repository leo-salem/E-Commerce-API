package API.com.example.E_COMMERCY.dto.category;


import API.com.example.E_COMMERCY.dto.category.request.NewCategoryRequestDto;
import API.com.example.E_COMMERCY.dto.product.ProductMapper;
import API.com.example.E_COMMERCY.dto.slimDTOs.ProductResponseSlimDto;
import API.com.example.E_COMMERCY.dto.user.UserMapper;
import API.com.example.E_COMMERCY.model.Category;
import API.com.example.E_COMMERCY.repository.ProductRepository;
import API.com.example.E_COMMERCY.service.product.ProductService;
import API.com.example.E_COMMERCY.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor

public class CategoryMapper {


    private final @Lazy UserMapper userMapper;

    private final @Lazy UserService userService;

    private final @Lazy ProductRepository productRepository;


    public CategoryResponseDto toDto(Category category) {
        return CategoryResponseDto.builder()
                .name(category.getName())
                .id((long) category.getId())
                .productResponseDtoSet(
                        category.getProducts().
                                stream()
                                .map(product -> ProductResponseSlimDto.builder()
                                        .id((long) product.getId())
                                        .name(product.getName())
                                        .price(product.getPrice())
                                        .build())
                                .collect(Collectors.toSet())
                )
                .userResponseDto(userMapper
                        .toDto(userService
                                .getCurrentUser(userService
                                        .getCurrentUsername())))
                .build();
    }

    public Category toEntity(NewCategoryRequestDto newCategoryRequestDto) {
     /*
        Set<Long> IDs = newCategoryRequestDto
                .getProductResponseDtoSet()
                .stream()
                .map(ProductResponseSlimDto::getId)
                .collect(Collectors.toSet());
*/
        return Category.builder()
                .name(newCategoryRequestDto.getName())
          /*      .products(IDs
                        .stream()
                        .map(productId -> productRepository.findById(productId)
                                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId)))
                        .collect(Collectors.toSet()))
           */
                .build();
    }

    public Category toEntity(CategoryResponseDto dto) {
        Set<Long> IDs = dto
                .getProductResponseDtoSet()
                .stream()
                .map(ProductResponseSlimDto::getId)
                .collect(Collectors.toSet());

        return Category.builder()
                .id(dto.getId().intValue())
                .name(dto.getName())
                .products(IDs
                        .stream()
                        .map(productId -> productRepository.findById(productId)
                                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId)))
                        .collect(Collectors.toSet()))
                .build();
    }


}
