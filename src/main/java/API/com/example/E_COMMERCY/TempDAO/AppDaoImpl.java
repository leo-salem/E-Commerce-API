package API.com.example.E_COMMERCY.TempDAO;

import API.com.example.E_COMMERCY.model.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class AppDaoImpl implements AppDao{
    EntityManager em;

    @Autowired
    public AppDaoImpl(EntityManager em) {
        this.em = em;
    }
    @Transactional
    @Override
    public void SaveUser(User user) {
        em.persist(user);
    }

    @Transactional
    @Override
    public void SaveCart(Cart cart) {
        em.persist(cart);
    }

    @Override
    public User findUserById(int id) {
        User user = em.find(User.class, id);
        //return user;
        return em.createQuery(
                        "SELECT DISTINCT u FROM User u " +
                                "LEFT JOIN FETCH u.orders o " +
                                "LEFT JOIN FETCH u.products p " +
                                "LEFT JOIN FETCH u.categories " +
                                "WHERE u.id = :id", User.class)
                .setParameter("id", id)
                .getSingleResult();

    }

    @Override
    @Transactional
    public void UpdateUser(User user) {
        em.merge(user);
    }

    @Transactional
    @Override
    public void deleteUserById(int theId) {
        User user = em.find(User.class, theId);

        /** in case it's user **/
        Set<Order> orders=user.getOrders();
        for(Order order:orders){
            Set<OrderItem>orderItems=order.getOrderItems();
            for(OrderItem orderItem:orderItems){
                Product product=orderItem.getProduct();
                Set<OrderItem>ProductOrderItems=product.getOrderitems();
                ProductOrderItems.remove(orderItem);
                orderItems.remove(orderItem);
            }
            order.setUser(null);
            em.remove(order);
        }
        user.getCart().setUser(null);
        em.remove(user.getCart());

        /** in case it's admin **/
        Set<Category>categories=user.getCategories();
        for(Category category:categories){
            Set<Product>products=category.getProducts();
            for(Product product:products){
                Set<OrderItem>orderItems=product.getOrderitems();
                Set<CartItem>cartItems=product.getCartItems();
                for(CartItem cartItem:cartItems){
                    cartItem.setProduct(null);
                    cartItem.getCart().getCartItems().remove(cartItem);
                }
                for(OrderItem orderItem:orderItems){
                    orderItem.getOrder().getOrderItems().remove(orderItem);
                }
                product.setCategory(null);
                em.remove(product);
            }
            category.getUser().getCategories().remove(category);
            em.remove(category);
        }
        Set<Product>products=user.getProducts();
        for(Product product:products){
            Category category=product.getCategory();
            category.getProducts().remove(product);
            Set<OrderItem>orderItems=product.getOrderitems();
            Set<CartItem>cartItems=product.getCartItems();
            for(CartItem cartItem:cartItems){
                cartItem.setProduct(null);
                cartItem.getCart().getCartItems().remove(cartItem);
            }
            for(OrderItem orderItem:orderItems){
                orderItem.setProduct(null);
                orderItem.getOrder().getOrderItems().remove(orderItem);
            }
            product.setCategory(null);
            em.remove(product);
        }
        em.remove(user);
    }

    @Transactional
    @Override
    public void SaveCategory(Category category){
        em.persist(category);
    }

    @Override
    public Category FindCategoryById(int id){
        Category category=em.find(Category.class, id);
        return em.createQuery(
                        "SELECT DISTINCT c FROM Category c " +
                                "LEFT JOIN FETCH c.products p " +
                                "LEFT JOIN FETCH c.user u " +
                                "WHERE c.id = :id", Category.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Transactional
    @Override
    public void UpdateCategory(Category category){
        em.merge(category);
    }

    @Transactional
    @Override
    public void DeleteCategoryById(int id){
        Category category=em.find(Category.class, id);
        Set<Product>products=category.getProducts();
        for(Product product:products){
            Set<OrderItem> orderItems=product.getOrderitems();
            Set<CartItem>cartItems=product.getCartItems();
            for(CartItem cartItem:cartItems){
                cartItem.setProduct(null);
                cartItem.getCart().getCartItems().remove(cartItem);
            }
            for(OrderItem orderItem:orderItems){
                orderItem.setProduct(null);
                orderItem.getOrder().getOrderItems().remove(orderItem);
            }
            product.setCategory(null);
            em.remove(product);
        }
        category.getUser().getCategories().remove(category);
        em.remove(category);
    }

}
