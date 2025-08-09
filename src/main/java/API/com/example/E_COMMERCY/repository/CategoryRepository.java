package API.com.example.E_COMMERCY.repository;

import API.com.example.E_COMMERCY.model.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @EntityGraph(attributePaths = {"products"})
    Optional<Category> findByName(String name);

    @EntityGraph(attributePaths = {"products"})
    List<Category> findAll();
}
