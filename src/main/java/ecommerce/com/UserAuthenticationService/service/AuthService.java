package ecommerce.com.UserAuthenticationService.service;

import ecommerce.com.UserAuthenticationService.model.dto.*;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<Response> register(RegisterRequest registerRequest);

    ResponseEntity<Response> login(LoginRequest loginRequest);

    ResponseEntity<Response> forgotPassword(String email);

    ResponseEntity<Response> resetPassword(ResetPasswordRequest request);
    }
