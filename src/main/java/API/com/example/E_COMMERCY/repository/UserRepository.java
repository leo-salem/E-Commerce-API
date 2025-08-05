package API.com.example.E_COMMERCY.repository;

import API.com.example.E_COMMERCY.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findAllByFirstNameAndLastName(String firstName, String lastName);

}
