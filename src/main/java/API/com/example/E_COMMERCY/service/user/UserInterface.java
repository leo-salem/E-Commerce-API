package API.com.example.E_COMMERCY.service.user;

import API.com.example.E_COMMERCY.dto.user.request.ChangeNameRequestDto;
import API.com.example.E_COMMERCY.dto.user.request.ChangePasswordRequestDto;
import API.com.example.E_COMMERCY.dto.user.request.DeleteUserRequestDto;
import API.com.example.E_COMMERCY.dto.user.UserResponseDto;
import API.com.example.E_COMMERCY.model.User;

import java.util.List;

public interface UserInterface {
    public UserResponseDto displayUserById(Long id);
    public List<UserResponseDto> displayUserByName(String FirstName, String LastName);
    public UserResponseDto displayUserByUsername(String Username);
    public String getCurrentUsername();
    public User getCurrentUser(String Username);
    public void ChangePassword(ChangePasswordRequestDto changePasswordRequestDto);
    public void ChangeName(ChangeNameRequestDto changeNameRequestDto);
    public void DeleteCurrentUser(DeleteUserRequestDto deleteUserRequestDto);
}
