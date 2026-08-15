package Expense_Calculator.Service;

import Expense_Calculator.Entity.PasswordResetTokenEntity;
import Expense_Calculator.Entity.RefreshTokenEntity;
import Expense_Calculator.Entity.UserEntity;
import Expense_Calculator.Enum.Role;
import Expense_Calculator.Repository.PasswordResetTokenRepository;
import Expense_Calculator.Repository.RefreshTokenRepository;
import Expense_Calculator.Repository.UserRepository;
import Expense_Calculator.RequestDTO.*;
import Expense_Calculator.ResponseDTO.AuthResponseDTO;
import Expense_Calculator.Security.CustomUserDetailsService;
import Expense_Calculator.Security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    //private final EmailService emailService;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       CustomUserDetailsService customUserDetailsService,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordResetTokenRepository passwordResetTokenRepository
                       //EmailService emailService
                       ){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        //this.emailService = emailService;
    }

    @Transactional
    public AuthResponseDTO login(LoginRequestDTO requestDTO){

       Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDTO.getEmail(),
                        requestDTO.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String accessToken =
                jwtService.generateAccessToken(userDetails);
        String refreshToken =
                jwtService.generateRefreshToken(userDetails);

        UserEntity user = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        System.out.println("DB password: " + user.getPassword());



        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();

        refreshTokenEntity.setToken(refreshToken);

        refreshTokenEntity.setUser(user);

        refreshTokenEntity.setExpiryDate(
                jwtService.getRefreshExpiryTime()
        );
        refreshTokenRepository.findByUser(user)
                .ifPresent(existingToken -> {
                    refreshTokenRepository.delete(existingToken);
                    refreshTokenRepository.flush();
                });

        refreshTokenRepository.save(refreshTokenEntity);

        return new AuthResponseDTO(
                accessToken,
                refreshToken
        );
    }

    @Transactional
    public void logout(LogoutRequestDTO request) {

        RefreshTokenEntity refreshTokenEntity =
                refreshTokenRepository.findByToken(
                        request.getRefreshToken()
                ).orElseThrow(() ->
                        new RuntimeException("Invalid Refresh Token"));

        refreshTokenRepository.delete(refreshTokenEntity);
    }

    public void register(RegisterRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        UserEntity user = new UserEntity();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    public AuthResponseDTO refreshToken(RefreshTokenRequestDTO request){

        String refreshToken = request.getRefreshToken();

        RefreshTokenEntity refreshTokenEntity =
                refreshTokenRepository.findByToken(refreshToken)
                        .orElseThrow(() ->
                                new RuntimeException("Invalid Refresh Token"));

        String username = jwtService.extractUsername(refreshToken);

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(username);

        if (jwtService.validateToken(refreshToken, userDetails)) {

            String accessToken = jwtService.generateAccessToken(userDetails);

            return new AuthResponseDTO(
                    accessToken,
                    refreshToken
            );

        } else {

            throw new RuntimeException("Invalid Refresh Token");

        }
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequestDTO requestDTO) {

        // 1. Find the user
        UserEntity user = userRepository
                .findByEmail(requestDTO.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // 2. Delete existing reset token
        passwordResetTokenRepository
                .deleteByUser(user);

        // Force DELETE to execute before INSERT
        passwordResetTokenRepository.flush();

        // 3. Generate a new reset token
        String resetToken = UUID.randomUUID().toString();

        // 4. Create reset token entity
        PasswordResetTokenEntity resetTokenEntity =
                new PasswordResetTokenEntity();

        resetTokenEntity.setToken(resetToken);
        resetTokenEntity.setUser(user);

        // 5. Set expiry - 15 minutes
        resetTokenEntity.setExpiryDate(
                LocalDateTime.now().plusMinutes(15)
        );

        // 6. Save it
        passwordResetTokenRepository.save(resetTokenEntity);

        // email sender
//        emailService.sendPasswordResetEmail(
//                user.getEmail(),
//                resetToken
//        );
        System.out.println("RESET TOKEN: " + resetToken);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequestDTO requestDTO) {

        System.out.println(
                "RESET TOKEN RECEIVED: " +
                        requestDTO.getResetToken()
        );

        // 1. Find the reset token
        PasswordResetTokenEntity resetTokenEntity =
                passwordResetTokenRepository
                        .findByToken(requestDTO.getResetToken())
                        .orElseThrow(() ->
                                new RuntimeException("Invalid reset token"));

        // 2. Check whether token has expired
        if (resetTokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            passwordResetTokenRepository.delete(resetTokenEntity);

            throw new RuntimeException("Reset token expired");
        }

        // 3. Get the user associated with the token
        UserEntity user = resetTokenEntity.getUser();

        // 4. Encode the new password
        user.setPassword(
                passwordEncoder.encode(requestDTO.getNewPassword())
        );

        // 5. Save updated user
        userRepository.save(user);

        // 6. Delete the reset token so it cannot be reused
        passwordResetTokenRepository.delete(resetTokenEntity);
    }
}
