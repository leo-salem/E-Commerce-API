package API.com.example.E_COMMERCY.repository;

import API.com.example.E_COMMERCY.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
