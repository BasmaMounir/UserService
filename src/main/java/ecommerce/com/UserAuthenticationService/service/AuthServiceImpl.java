package ecommerce.com.UserAuthenticationService.service;

import ecommerce.com.UserAuthenticationService.exception.UserAlreadyExistsException;
import ecommerce.com.UserAuthenticationService.mapper.UserMapper;
import ecommerce.com.UserAuthenticationService.model.Entity.User;
import ecommerce.com.UserAuthenticationService.model.dto.*;
import ecommerce.com.UserAuthenticationService.rabbit.UserEvent;
import ecommerce.com.UserAuthenticationService.rabbit.UserProducer;
import ecommerce.com.UserAuthenticationService.repository.UserRepository;
import ecommerce.com.UserAuthenticationService.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final UserProducer userProducer;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<Response> register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("A user with this email already exists");
        }

        User user = userMapper.toEntity(registerRequest);
        userRepository.save(user);

        String jwt = jwtService.generateToken(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new Response("User registered successfully!", registerRequest.getEmail(), jwt)
        );
    }

    @Override
    public ResponseEntity<Response> login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String jwt = jwtService.generateToken(user);
            return ResponseEntity.ok(
                    new Response("User logged in successfully!", loginRequest.getEmail(), jwt));
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid email or password!");
        }
    }

    @Override
    public ResponseEntity<Response> forgotPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Response("There is no user registered with this email address " + email));
        }

        String resetCode = String.valueOf(new Random().nextInt(900000) + 100000);
        User user = userOptional.get();
        user.setResetCode(resetCode);
        user.setResetCodeExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        UserEvent message = new UserEvent(email, resetCode);
        userProducer.sendCodeMessage(message);

        return ResponseEntity.ok(new Response("Reset code sent to your email"));
    }

    public ResponseEntity<Response> resetPassword(ResetPasswordRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("User not found"));
        }

        User user = userOptional.get();
        if (user.getResetCode() == null || !user.getResetCode().equals(request.getResetCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Invalid or missing reset code"));
        }

        if (user.getResetCodeExpiry() == null || user.getResetCodeExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Reset code has expired"));
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetCode(null);
        user.setResetCodeExpiry(null);
        userRepository.save(user);

        return ResponseEntity.ok(new Response("Password reset successfully"));
    }
}