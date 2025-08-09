package API.com.example.E_COMMERCY.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cartitem")
@Data
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "Quantity",nullable = false)
    private int Quantity;


    @ManyToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "ProductId", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "CartId", nullable = false)
    private Cart cart;


    public CartItem() {
    }

    public CartItem( int Quantity) {
        this.Quantity = Quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", Quantity=" + Quantity +
                '}';
    }
}
