package Expense_Calculator.Service;

import Expense_Calculator.Entity.RefreshTokenEntity;
import Expense_Calculator.Entity.UserEntity;
import Expense_Calculator.Enum.Role;
import Expense_Calculator.Repository.RefreshTokenRepository;
import Expense_Calculator.Repository.UserRepository;
import Expense_Calculator.RequestDTO.LoginRequestDTO;
import Expense_Calculator.RequestDTO.LogoutRequestDTO;
import Expense_Calculator.RequestDTO.RefreshTokenRequestDTO;
import Expense_Calculator.RequestDTO.RegisterRequestDTO;
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
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       CustomUserDetailsService customUserDetailsService,
                       RefreshTokenRepository refreshTokenRepository){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
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

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();

        refreshTokenEntity.setToken(refreshToken);

        refreshTokenEntity.setUser(user);

        refreshTokenEntity.setExpiryDate(
                jwtService.getRefreshExpiryTime()
        );
        refreshTokenRepository.findByUser(user)
                .ifPresent(refreshTokenRepository::delete);

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
}
