package API.com.example.E_COMMERCY.repository;

import API.com.example.E_COMMERCY.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static ch.qos.logback.core.joran.spi.ConsoleTarget.findByName;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    List<Product> findAll();
}
