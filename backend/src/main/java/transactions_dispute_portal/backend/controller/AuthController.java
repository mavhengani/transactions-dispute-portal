package transactions_dispute_portal.backend.controller;

import org.springframework.web.bind.annotation.*;

import transactions_dispute_portal.backend.dto.AuthRequestDTO;
import transactions_dispute_portal.backend.dto.AuthResponseDTO;
import transactions_dispute_portal.backend.dto.RegisterRequest;
import transactions_dispute_portal.backend.dto.UserResponseDTO;
import transactions_dispute_portal.backend.entity.User;
import transactions_dispute_portal.backend.repository.UserRepository;
import transactions_dispute_portal.backend.service.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(
            UserRepository repo,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());

        // 🔥 FIX: hash password
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setCellphone(request.getCellphone());
        user.setGender(request.getGender());
        user.setCardNumber(request.getCardNumber());

        return repo.save(user);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtService.generateToken(userDetails);

        // Get the user entity to create UserResponseDTO
        User user = repo.findByUsername(request.getUsername()).orElseThrow();

        UserResponseDTO userResponse = new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getCellphone()
        );

        return new AuthResponseDTO(jwtToken, userResponse);
    }
}