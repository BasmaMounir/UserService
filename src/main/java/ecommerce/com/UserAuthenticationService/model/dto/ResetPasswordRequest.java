package ecommerce.com.UserAuthenticationService.model.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String email;
    private String resetCode;
    private String newPassword;
}
