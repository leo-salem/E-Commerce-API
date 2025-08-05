package API.com.example.E_COMMERCY.model;

import API.com.example.E_COMMERCY.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
@Data
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", unique = true,nullable = false)
    private String username;

    @Column(name = "hasRole",nullable = false)
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
    private Cart cart;

    @OneToMany(mappedBy = "user",
             fetch = FetchType.LAZY,   //EAGER temporarly until i reach it
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private Set<Order> orders;

    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY,   //EAGER temporarly until i reach it
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private Set<Product> products;

    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY,   //EAGER temporarly until i reach it
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private Set<Category>categories;

    public User() {

    }

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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", adress='" + adress + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
