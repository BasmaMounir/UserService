package ecommerce.com.UserAuthenticationService.service;

import ecommerce.com.UserAuthenticationService.model.Entity.Role;
import ecommerce.com.UserAuthenticationService.model.Entity.User;
import ecommerce.com.UserAuthenticationService.repository.UserRepository;
import ecommerce.com.UserAuthenticationService.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found!"));
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateUserRole(Long userId, String newRole) {
        return userRepository.findById(userId).map(user -> {
            try {
                Role role = Role.valueOf(newRole.toUpperCase());
                user.setRole(role);
                userRepository.save(user);
                return ResponseEntity.ok("User role updated successfully to " + newRole);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        "Invalid role specified: " + newRole + ". Valid roles are: " + List.of(Role.values()));
            }
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + userId));
    }

    @Override
    @Transactional
    public ResponseEntity<String> activateUser(Long userId) {
        return userRepository.findById(userId).map(user -> {
            if (user.isEnabled()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already active");
            }
            user.setEnabled(true);
            userRepository.save(user);
            return ResponseEntity.ok("User activated successfully");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }

    @Override
    @Transactional
    public ResponseEntity<String> deactivateUser(Long userId) {
        return userRepository.findById(userId).map(user -> {
            if (!user.isEnabled()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already deactivated");
            }
            user.setEnabled(false);
            userRepository.save(user);
            return ResponseEntity.ok("User deactivated successfully");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteUser(Long userId) {
        return userRepository.findById(userId).map(user -> {
            userRepository.delete(user);
            return ResponseEntity.ok("User deleted successfully");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }

    @Override
    public boolean checkUserRole(String token, String requiredRole) {
        if (token == null || !token.startsWith("Bearer ")) {
            return false;
        }
        token = token.substring(7);
        try {
            String extractedRole = jwtService.extractRole(token);
            return extractedRole.equalsIgnoreCase("ROLE_" + requiredRole);
        } catch (Exception e) {
            return false;
        }
    }
}