package ecommerce.com.UserAuthenticationService.service;

import ecommerce.com.UserAuthenticationService.model.dto.AuthResponse;
import ecommerce.com.UserAuthenticationService.model.dto.LoginRequest;
import ecommerce.com.UserAuthenticationService.model.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<AuthResponse> register(RegisterRequest registerRequest);

    ResponseEntity<AuthResponse> login(LoginRequest loginRequest);

}
