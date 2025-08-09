package API.com.example.E_COMMERCY.dto.category;

import API.com.example.E_COMMERCY.dto.product.ProductResponseDto;
import API.com.example.E_COMMERCY.dto.slimDTOs.ProductResponseSlimDto;
import API.com.example.E_COMMERCY.dto.user.UserResponseDto;
import API.com.example.E_COMMERCY.model.Product;
import API.com.example.E_COMMERCY.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponseDto {

    private Long id;

    private String name;

    private Set<ProductResponseSlimDto> productResponseDtoSet;

    private UserResponseDto userResponseDto;

}
