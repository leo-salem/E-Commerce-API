package API.com.example.E_COMMERCY.dto.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeProductNameRequestDto {
    @NotBlank
    private String name;

    @NotBlank
    private String newName;
}
