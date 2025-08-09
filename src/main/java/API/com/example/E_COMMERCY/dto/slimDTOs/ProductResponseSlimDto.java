package API.com.example.E_COMMERCY.dto.slimDTOs;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseSlimDto {

    private Long id;

    private String name;

    private double price;

}
