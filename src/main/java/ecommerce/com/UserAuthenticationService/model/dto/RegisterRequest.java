package ecommerce.com.UserAuthenticationService.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one number")
    private String password;

    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    @Pattern(regexp = "^01[0-2,5]{1}[0-9]{8}$", message = "Invalid Egyptian phone number")
    private String phoneNumber;

    @NotBlank(message = "Address must not be blank")
    private String address;

    @Pattern(regexp = "ROLE_USER|ROLE_ADMIN", message = "Role must be ROLE_USER or ROLE_ADMIN")
    private String role;
}