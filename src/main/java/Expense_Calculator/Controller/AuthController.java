package Expense_Calculator.Controller;

import Expense_Calculator.RequestDTO.LoginRequestDTO;
import Expense_Calculator.RequestDTO.LogoutRequestDTO;
import Expense_Calculator.RequestDTO.RefreshTokenRequestDTO;
import Expense_Calculator.RequestDTO.RegisterRequestDTO;
import Expense_Calculator.ResponseDTO.AuthResponseDTO;
import Expense_Calculator.Service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO>  login(@RequestBody LoginRequestDTO request){
        AuthResponseDTO response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequestDTO request) {

        authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User Registered Successfully");
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO request){
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestBody LogoutRequestDTO request) {

        authService.logout(request);

        return ResponseEntity.ok("Logout Successful");
    }
}
