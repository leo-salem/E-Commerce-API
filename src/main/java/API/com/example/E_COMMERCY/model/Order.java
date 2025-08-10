package API.com.example.E_COMMERCY.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Table(name = "orders")
@Entity
@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"user", "orderItems"})
public class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "total",nullable = false)
    private double total;

    @Column(name = "PaymentMethod",nullable = false)
    private String PaymentMethod;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="UserId")
    @JsonIgnore
    private User user;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "order",cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JsonIgnore
    private Set<OrderItem> orderItems;

    public Order(Date date, double total, String PaymentMethod) {
        this.date = date;
        this.total = total;
        this.PaymentMethod = PaymentMethod;
    }

    public void addOrdertem(OrderItem orderItem) {
        if (orderItems == null) {
            orderItems = new HashSet<>();
        }
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
}
