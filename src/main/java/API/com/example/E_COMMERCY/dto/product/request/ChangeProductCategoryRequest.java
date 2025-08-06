package API.com.example.E_COMMERCY.dto.product.request;

import API.com.example.E_COMMERCY.dto.category.CategoryResponseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeProductCategoryRequest {
    @NotBlank
    private String name;
    @NotBlank
    private CategoryResponseDto categoryResponseDto;
}
