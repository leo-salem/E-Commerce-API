package API.com.example.E_COMMERCY.service.deletion;

import API.com.example.E_COMMERCY.model.Cart;
import API.com.example.E_COMMERCY.model.Category;
import API.com.example.E_COMMERCY.model.Order;
import API.com.example.E_COMMERCY.model.Product;

public interface DeletionInterface {
        void deleteOrder(Order order);
        void deleteProduct(Product product);
        void deleteCategory(Category category);
        void deleteCart(Cart cart);
}
