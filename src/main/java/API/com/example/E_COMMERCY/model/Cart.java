package API.com.example.E_COMMERCY.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Table(name = "cart")
@Entity
@Data
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @OneToOne(mappedBy = "cart",
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private User user;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "cart",cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private Set<CartItem> cartItems;

    public Cart() {

    }

    public void addCartItem(CartItem cartItem) {
        if (cartItems == null) {
            cartItems = new HashSet<>() {
            };
        }
        cartItems.add(cartItem);
        cartItem.setCart(this);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", user=" + user +
               // ", cartItems=" + cartItems +
                '}';
    }
}
