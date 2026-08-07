package Expense_Calculator.Security;

import Expense_Calculator.Entity.UserEntity;
import Expense_Calculator.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        System.out.println("email received " + username);
        UserEntity user = userRepository
                .findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
        System.out.println("User found : " + user.getEmail());

        return new CustomUserDetails(user);
    }

}
