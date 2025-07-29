package ecommerce.com.UserAuthenticationService.rabbit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserEvent {
    private String customerEmail;
    private String code;
}