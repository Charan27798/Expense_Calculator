package Expense_Calculator.Service;


import Expense_Calculator.RequestDTO.UserRequestDTO;
import Expense_Calculator.Entity.UserEntity;
import Expense_Calculator.Repository.UserRepository;
import Expense_Calculator.ResponseDTO.UserResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO){

        UserEntity user = new UserEntity();

        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());

       UserEntity savedUser =  userRepository.save(user);
         return maptoResponseDTo(savedUser);

    }

    private UserResponseDTO maptoResponseDTo(UserEntity user){

        UserResponseDTO response = new UserResponseDTO();
          response.setId(user.getId());
          response.setName(user.getName());
          response.setEmail(user.getEmail());
          return response;
    }


}
