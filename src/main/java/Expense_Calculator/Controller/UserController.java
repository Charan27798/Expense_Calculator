package Expense_Calculator.Controller;

import Expense_Calculator.RequestDTO.UserRequestDTO;
import Expense_Calculator.ResponseDTO.UserResponseDTO;
import Expense_Calculator.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

//    @Autowired
//    public UserService userService;

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/createUser")
    public UserResponseDTO createUser(@Valid @RequestBody UserRequestDTO dto){

      return userService.createUser(dto);
    }
}
