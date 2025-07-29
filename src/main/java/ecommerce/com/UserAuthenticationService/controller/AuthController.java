package ecommerce.com.UserAuthenticationService.controller;

import ecommerce.com.UserAuthenticationService.model.dto.LoginRequest;
import ecommerce.com.UserAuthenticationService.model.dto.RegisterRequest;
import ecommerce.com.UserAuthenticationService.model.dto.ResetPasswordRequest;
import ecommerce.com.UserAuthenticationService.model.dto.Response;
import ecommerce.com.UserAuthenticationService.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Response> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        return authService.forgotPassword(email);
    }
    @PostMapping("/reset-password")
    public ResponseEntity<Response> resetPassword(@RequestBody ResetPasswordRequest request) {
    return authService.resetPassword(request);
    }

    }