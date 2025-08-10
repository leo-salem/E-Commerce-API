package API.com.example.E_COMMERCY.dto.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeProductPriceDto {
    @NotBlank
    private String name;

    @Positive
    private double price;
}
