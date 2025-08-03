package API.com.example.E_COMMERCY.model;

import API.com.example.E_COMMERCY.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
@Data
@AllArgsConstructor
@Builder
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
    private String LastName;

    @Column(name = "firstName",nullable = false)
    private String FirstName;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setEmail(String username) {
        this.username = username;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public Role getHasRole() {
        return hasRole;
    }

    public void setHasRole(Role hasRole) {
        this.hasRole = hasRole;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
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
