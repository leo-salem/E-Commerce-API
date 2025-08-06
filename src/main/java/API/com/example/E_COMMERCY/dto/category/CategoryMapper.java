package API.com.example.E_COMMERCY.dto.category;


import API.com.example.E_COMMERCY.dto.category.request.NewCategoryRequestDto;
import API.com.example.E_COMMERCY.dto.product.ProductMapper;
import API.com.example.E_COMMERCY.dto.user.UserMapper;
import API.com.example.E_COMMERCY.model.Category;
import API.com.example.E_COMMERCY.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryMapper {

    private final @Lazy ProductMapper productMapper;

    private final  UserMapper userMapper;

    private final UserService userService;


    public CategoryResponseDto toDto(Category category) {
        return CategoryResponseDto.builder()
                .name(category.getName())
                .id(category.getId())
                .productResponseDtoSet(
                        category.getProducts().
                                stream()
                                .map(productMapper::toDto)
                                .collect(Collectors.toSet())
                )
                .userResponseDto(userMapper
                        .toDto(userService
                                .getCurrentUser(userService
                                        .getCurrentUsername())))
                .build();
    }
    public Category toEntity(NewCategoryRequestDto newCategoryRequestDto) {
        return Category.builder()
                .name(newCategoryRequestDto.getName())
                .products(newCategoryRequestDto
                        .getProductResponseDtoSet()
                        .stream()
                        .map(productMapper::toEntity)
                        .collect(Collectors.toSet()))
                .build();
    }

    public Category toEntity(CategoryResponseDto dto) {
        return Category.builder()
                .id(dto.getId())
                .name(dto.getName())
                .products(dto.getProductResponseDtoSet().stream()
                        .map(productMapper::toEntity)
                        .collect(Collectors.toSet()))
                .build();
    }

}
