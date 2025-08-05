package API.com.example.E_COMMERCY.repository;

import API.com.example.E_COMMERCY.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
}
