package API.com.example.E_COMMERCY.TempDAO;

import API.com.example.E_COMMERCY.model.Cart;
import API.com.example.E_COMMERCY.model.Category;
import API.com.example.E_COMMERCY.model.User;

public interface AppDao {
     void SaveUser(User user);
     void SaveCart(Cart cart);
     User findUserById(int id);
     void deleteUserById(int theId);
     void UpdateUser(User user);
     void SaveCategory(Category category);
     Category FindCategoryById(int id);
     void UpdateCategory(Category category);
     void DeleteCategoryById(int theId);

}
