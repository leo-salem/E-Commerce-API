package API.com.example.E_COMMERCY.dto.category.request;

import API.com.example.E_COMMERCY.dto.product.ProductResponseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewCategoryRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private Set<ProductResponseDto> productResponseDtoSet;


}
