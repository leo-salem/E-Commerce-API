package API.com.example.E_COMMERCY.repository;

import API.com.example.E_COMMERCY.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
