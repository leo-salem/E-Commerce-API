package API.com.example.E_COMMERCY.controller;

import API.com.example.E_COMMERCY.dto.user.UserResponseDto;
import API.com.example.E_COMMERCY.dto.user.request.ChangePasswordRequestDto;
import API.com.example.E_COMMERCY.dto.user.request.DeleteUserRequestDto;
import API.com.example.E_COMMERCY.dto.user.request.NameRequestDto;
import API.com.example.E_COMMERCY.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/UserSection")
public class UserController {

    private final UserService userService;

  //  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/TheId/{id}")
    public ResponseEntity<?> displayByTheId(@PathVariable Long id){
        UserResponseDto userResponseDto=userService.displayUserById(id);
        return ResponseEntity.ok().body(userResponseDto);
    }

    @GetMapping("/user/TheUsername/{username}")
    public ResponseEntity<?> displayByTheUsername(@PathVariable String username){
        UserResponseDto userResponseDto=userService.displayUserByUsername(username);
        return ResponseEntity.ok().body(userResponseDto);
    }

    @PostMapping("/user/changeTheName")
    public ResponseEntity<?> ChangeTheName(@RequestBody @Valid NameRequestDto nameRequestDto){
        userService.ChangeName(nameRequestDto);
        return ResponseEntity.ok("name changed successfully");
    }

    @PostMapping("/user/changeThePassword")
    public ResponseEntity<?> ChangeThePassword(@RequestBody @Valid ChangePasswordRequestDto passwordRequestDto){
        userService.ChangePassword(passwordRequestDto);
        return ResponseEntity.ok("password changed successfully");
    }

    @PostMapping("/user/delete")
    public ResponseEntity<?> deleteTheUser(@RequestBody @Valid DeleteUserRequestDto deleteUserRequestDto){
       // try {
            userService.DeleteCurrentUser(deleteUserRequestDto);
            return ResponseEntity.ok("deleted user successfully");

       /* } catch (Exception e) {
            e.printStackTrace(); //
            return ResponseEntity.status(500).body("Error deleting user: " + e.getMessage());
        }
        */
    }



}
