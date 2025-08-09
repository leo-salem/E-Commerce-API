package API.com.example.E_COMMERCY.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orderitem")
@Data
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "Quantity",nullable = false)
    private int Quantity;

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public OrderItem() {
    }

    public OrderItem(int Quantity) {
        this.Quantity = Quantity;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", Quantity=" + Quantity +
                '}';
    }
}
