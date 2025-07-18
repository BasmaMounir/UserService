package ecommerce.com.UserAuthenticationService.controller;

import ecommerce.com.UserAuthenticationService.model.dto.AuthResponse;
import ecommerce.com.UserAuthenticationService.model.dto.LoginRequest;
import ecommerce.com.UserAuthenticationService.model.dto.RegisterRequest;
import ecommerce.com.UserAuthenticationService.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}