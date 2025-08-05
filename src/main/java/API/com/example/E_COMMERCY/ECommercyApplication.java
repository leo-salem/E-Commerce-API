package API.com.example.E_COMMERCY;

import API.com.example.E_COMMERCY.TempDAO.AppDao;
import API.com.example.E_COMMERCY.model.Cart;
import API.com.example.E_COMMERCY.model.Category;
import API.com.example.E_COMMERCY.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ECommercyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommercyApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AppDao appDao) {

		return runner -> {


		};
	}
	private void createUserAndCart(AppDao appDao) {
		User user = new User("salem@gmail.com","password","elmo3ez street");
		Cart cart = new Cart();
		user.setCart(cart);
		cart.setUser(user);
		appDao.SaveUser(user);
//		System.out.println(cart.getUser());
	}

	private void findUserAndCartByUserId(AppDao appDao) {
		User user= appDao.findUserById(1);
		Cart cart=user.getCart();
		System.out.println(user+"\n"+cart);
	}

	private void UpdateTheUser(AppDao appDao) {
		User user = appDao.findUserById(1);
		user.setUsername("LeoSalem@gmail.com");
		appDao.UpdateUser(user);
	}

	private void DeleteUserById(AppDao appDao) {
		appDao.deleteUserById(1);
	}

	private void createUserAdminAndCategory(AppDao appDao) {
		User user=new User("leo@gmail.com","password","elmo3ez street");
		Category category1=new Category("Meet section");
		Category category2=new Category("clothes section");
		user.addCategory(category1);
		user.addCategory(category2);
		appDao.SaveUser(user);
	}






}
