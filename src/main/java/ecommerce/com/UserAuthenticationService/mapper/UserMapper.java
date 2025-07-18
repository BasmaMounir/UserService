package ecommerce.com.UserAuthenticationService.mapper;

import ecommerce.com.UserAuthenticationService.model.Entity.Role;
import ecommerce.com.UserAuthenticationService.model.Entity.User;
import ecommerce.com.UserAuthenticationService.model.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User toEntity(RegisterRequest registerRequest){
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setAddress(registerRequest.getAddress());
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);
        return user;
    }

}