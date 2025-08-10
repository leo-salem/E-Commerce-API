package API.com.example.E_COMMERCY.model;

import API.com.example.E_COMMERCY.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = {"orders", "products", "categories", "cart"})
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", unique = true,nullable = false)
    private String username;

    @Column(name = "has_role",nullable = false)
    @Enumerated(EnumType.STRING)
    private Role hasRole;

    @Column(name = "lastName",nullable = false)
    private String lastName;

    @Column(name = "firstName",nullable = false)
    private String firstName;

    @Column(name = "adress",nullable = false)
    private String adress;

    @Column(name = "password",nullable = false)
    private String password;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "CartId")
    @JsonIgnore
    private Cart cart;

    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JsonIgnore
    private Set<Order> orders;

    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JsonIgnore
    private Set<Product> products;

    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JsonIgnore
    private Set<Category>categories;

    public User(String username, String password, String adress) {
        this.username = username;
        this.password = password;
        this.adress = adress;
    }

    public void addProduct(Product product) {
        if (products == null) {
            products = new HashSet<>();
        }
        products.add(product);
        product.setUser(this);
    }

    public void addCategory(Category category) {
        if (categories == null) {
            categories = new HashSet<>();
        }
        categories.add(category);
        category.setUser(this);
    }
}
