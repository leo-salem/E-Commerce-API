package API.com.example.E_COMMERCY.dto.product;

import API.com.example.E_COMMERCY.dto.slimDTOs.CategoryResponseSlimDto;
import API.com.example.E_COMMERCY.dto.user.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDto {

    private Long id;

    private String name;

    private double price;

    private String description;

    private CategoryResponseSlimDto categoryResponseDto;

    private UserResponseDto userResponseDto;
}
