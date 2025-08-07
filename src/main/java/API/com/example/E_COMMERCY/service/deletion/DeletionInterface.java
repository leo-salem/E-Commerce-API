package API.com.example.E_COMMERCY.service.deletion;

import API.com.example.E_COMMERCY.model.*;

public interface DeletionInterface {
        void deleteOrder(Order order);
        void deleteProduct(Product product);
        void deleteCategory(Category category);
        void deleteCart(Cart cart);
        void deleteCartItem(CartItem cartItem);
}
