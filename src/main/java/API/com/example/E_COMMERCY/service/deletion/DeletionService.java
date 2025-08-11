package API.com.example.E_COMMERCY.service.deletion;

import API.com.example.E_COMMERCY.model.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DeletionService implements DeletionInterface{

    private final EntityManager em;

    public DeletionService(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public void deleteOrder(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            if (product != null) {
                product.getOrderitems().remove(orderItem);
            }
            em.remove(em.contains(orderItem) ? orderItem : em.merge(orderItem));
        }
        order.getOrderItems().clear();
        if (order.getUser() != null) {
            order.getUser().getOrders().remove(order);
            order.setUser(null);
        }
        em.flush();
        em.remove(em.contains(order) ? order : em.merge(order));
    }

    @Override
    @Transactional
    public void deleteProduct(Product product) {
        if (product.getCategory() != null) {
            product.getCategory().getProducts().remove(product);
            product.setCategory(null);
        }

        for (CartItem cartItem : product.getCartItems()) {
            if (cartItem.getCart() != null) {
                cartItem.getCart().getCartItems().remove(cartItem);
            }
            cartItem.setProduct(null);
        }

        for (OrderItem orderItem : product.getOrderitems()) {
            if (orderItem.getOrder() != null) {
                orderItem.getOrder().getOrderItems().remove(orderItem);
            }
            orderItem.setProduct(null);
        }

        product.getCartItems().clear();
        product.getOrderitems().clear();

        em.remove(em.contains(product) ? product : em.merge(product));
    }

    @Override
    @Transactional
    public void deleteCategory(Category category) {
        for (Product product : category.getProducts()) {
            deleteProduct(product);  // Use deleteProduct to handle all cascading logic
        }
        category.getProducts().clear();
        category.setUser(null);
        em.remove(em.contains(category) ? category : em.merge(category));
    }

    @Override
    @Transactional
    public void deleteCart(Cart cart) {
        for (CartItem cartItem : cart.getCartItems()) {
            cartItem.setCart(null);
            cartItem.setProduct(null);
        }
        cart.getCartItems().clear();
        cart.setUser(null);
        em.remove(em.contains(cart) ? cart : em.merge(cart));
    }
    @Transactional
    @Override
    public void deleteCartItem(CartItem cartItem) {
        if (cartItem.getCart()!=null){
            cartItem.getCart().getCartItems().remove(cartItem);
        }
        if (cartItem.getProduct()!=null){
            cartItem.getProduct().getCartItems().remove(cartItem);
        }
        cartItem.setCart(null);
        cartItem.setProduct(null);
        em.remove(em.contains(cartItem) ? cartItem : em.merge(cartItem));
    }


}
