package API.com.example.E_COMMERCY;

import API.com.example.E_COMMERCY.model.Order;
import API.com.example.E_COMMERCY.model.User;
import API.com.example.E_COMMERCY.repository.OrderRepository;
import API.com.example.E_COMMERCY.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import API.com.example.E_COMMERCY.enums.Role;

@SpringBootApplication
public class ECommercyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommercyApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return runner -> {
			String adminUsername = "admin";
			if (userRepository.findByUsername(adminUsername).isEmpty()) {
				User adminUser = User.builder()
						.username(adminUsername)
						.firstName("System")
						.lastName("Admin")
						.adress("Head Office")
						.password(passwordEncoder.encode("admin123"))
						.hasRole(Role.ADMIN)
						.build();

				userRepository.save(adminUser);
				System.out.println(" Admin user created successfully!");
			} else {
				System.out.println(" Admin user already exists.");
			}
		};
	}

}
