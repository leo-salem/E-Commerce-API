package API.com.example.E_COMMERCY.dto.product.request;

import API.com.example.E_COMMERCY.dto.category.CategoryResponseDto;
import API.com.example.E_COMMERCY.dto.slimDTOs.CategoryResponseSlimDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeProductCategoryRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private CategoryResponseSlimDto categoryResponseSlimDto;
}
