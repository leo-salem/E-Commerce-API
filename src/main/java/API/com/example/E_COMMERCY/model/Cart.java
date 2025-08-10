package API.com.example.E_COMMERCY.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Table(name = "cart")
@Entity
@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"user", "cartItems"})
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @OneToOne(mappedBy = "cart",
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonIgnore
    private User user;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "cart",cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JsonIgnore
    private Set<CartItem> cartItems;

    public void addCartItem(CartItem cartItem) {
        if (cartItems == null) {
            cartItems = new HashSet<>();
        }
        cartItems.add(cartItem);
        cartItem.setCart(this);
    }
}
