package ecommerce.com.UserAuthenticationService.service;

import ecommerce.com.UserAuthenticationService.exception.UserAlreadyExistsException;
import ecommerce.com.UserAuthenticationService.mapper.UserMapper;
import ecommerce.com.UserAuthenticationService.model.Entity.User;
import ecommerce.com.UserAuthenticationService.model.dto.AuthResponse;
import ecommerce.com.UserAuthenticationService.model.dto.LoginRequest;
import ecommerce.com.UserAuthenticationService.model.dto.RegisterRequest;
import ecommerce.com.UserAuthenticationService.repository.UserRepository;
import ecommerce.com.UserAuthenticationService.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<AuthResponse> register(RegisterRequest registerRequest) {

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("A user with this email already exists");
        }
        User user = userMapper.toEntity(registerRequest);
        userRepository.save(user);
        String jwt = jwtService.generateToken(user);
        return new ResponseEntity<>(
                new AuthResponse("User registered successfully!", registerRequest.getEmail(), jwt),
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<AuthResponse> login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String jwt = jwtService.generateToken(user);
        return new ResponseEntity<>(new AuthResponse("User logged in successfully!", loginRequest.getEmail(), jwt), HttpStatus.OK);
    }

}