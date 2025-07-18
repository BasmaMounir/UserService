package ecommerce.com.UserAuthenticationService.service;

import ecommerce.com.UserAuthenticationService.model.Entity.Role;
import ecommerce.com.UserAuthenticationService.model.Entity.User;
import ecommerce.com.UserAuthenticationService.repository.UserRepository;
import ecommerce.com.UserAuthenticationService.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Transactional
    public ResponseEntity<String> updateUserRole(Long userId, String newRole) {
        return userRepository.findById(userId).map(user -> {
            try {
                Role role = Role.valueOf(newRole.toUpperCase());
                user.setRole(role);
                userRepository.save(user);
                return ResponseEntity.ok("User role updated successfully to " + newRole);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>("Invalid role specified: " + newRole + ". Valid roles are: " + List.of(Role.values()), HttpStatus.BAD_REQUEST);
            }
        }).orElse(new ResponseEntity<>("User not found with ID: " + userId, HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public ResponseEntity<String> activateUser(Long userId) {
        return userRepository.findById(userId).map(user -> {
            if (user.isEnabled()) {
                return new ResponseEntity<>("User with ID: " + userId + " is already active.", HttpStatus.CONFLICT);
            }
            user.setEnabled(true);
            userRepository.save(user);
            return ResponseEntity.ok("User with ID: " + userId + " activated successfully.");
        }).orElse(new ResponseEntity<>("User not found with ID: " + userId, HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public ResponseEntity<String> deactivateUser(Long userId) {
        return userRepository.findById(userId).map(user -> {
            if (!user.isEnabled()) {
                return new ResponseEntity<>("User with ID: " + userId + " is already deactivated.", HttpStatus.CONFLICT);
            }
            user.setEnabled(false);
            userRepository.save(user);
            return ResponseEntity.ok("User with ID: " + userId + " deactivated successfully.");
        }).orElse(new ResponseEntity<>("User not found with ID: " + userId, HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("User not found with ID: " + userId, HttpStatus.NOT_FOUND);
        }
        userRepository.delete(userOptional.get());
        return ResponseEntity.ok("User with ID: " + userId + " deleted successfully.");
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