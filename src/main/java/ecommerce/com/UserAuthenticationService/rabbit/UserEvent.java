package ecommerce.com.UserAuthenticationService.rabbit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserEvent implements Serializable {
    private String customerEmail;
    private String code;
}