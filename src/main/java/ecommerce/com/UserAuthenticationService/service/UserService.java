package ecommerce.com.UserAuthenticationService.service;

import ecommerce.com.UserAuthenticationService.model.Entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    ResponseEntity<String> updateUserRole(Long userId, String newRole);

    ResponseEntity<String> activateUser(Long userId);

    ResponseEntity<String> deactivateUser(Long userId);

    ResponseEntity<String> deleteUser(Long userId);

    boolean checkUserRole(String token, String role);
}
