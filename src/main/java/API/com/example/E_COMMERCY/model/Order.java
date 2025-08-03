package API.com.example.E_COMMERCY.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.*;

@Table(name = "orders")
@Entity
@Data
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "amount",nullable = false)
    private int amount;

    @Column(name = "PaymentMethod",nullable = false)
    private String PaymentMethod;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="UserId")
    private User user;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "order",cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private Set<OrderItem> orderItems;


    public Order() {

    }

    public Order(Date date, int amount, String PaymentMethod) {
        this.date = date;
        this.amount = amount;
        this.PaymentMethod = PaymentMethod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItem) {
        this.orderItems = orderItem;
    }

    public void addOrdertem(OrderItem orderItem) {
        if (orderItems == null) {
            orderItems = new HashSet<>();
        }
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
}
