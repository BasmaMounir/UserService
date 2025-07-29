package ecommerce.com.UserAuthenticationService.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private String message;
    private String email;
    private String token;
    private LocalDateTime timestamp;

    public Response(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public Response(String message, String email, String token) {
        this.message = message;
        this.email = email;
        this.token = token;
        this.timestamp = LocalDateTime.now();

    }
}

