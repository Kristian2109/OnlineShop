package online.shops.simple.controllers;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;

import lombok.AllArgsConstructor;
import lombok.Data;
import online.shops.simple.events.UserActivityEvent;
import online.shops.simple.models.Account;
import online.shops.simple.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Data
    @AllArgsConstructor
    static class AuthRequest {
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    static class AuthResponse {
        private String token;
    }

    private final AuthService authService;
    private final KafkaTemplate<String, UserActivityEvent> kafkaTemplate;

    public AuthController(AuthService authService, KafkaTemplate<String, UserActivityEvent> kafkaTemplate) {
        this.authService = authService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) throws JOSEException {
        String token = authService.login(request.getUsername(), request.getPassword());
        UserActivityEvent event = new UserActivityEvent("USER_LOGGED_IN", null, request.getUsername(), LocalDateTime.now());
        kafkaTemplate.send("user-activity", event);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest registerRequest) {
        if (authService.isUsernameExists(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        Account account = authService.registerUser(registerRequest.getUsername(), registerRequest.getPassword());

        UserActivityEvent event = new UserActivityEvent("USER_REGISTERED", account.getId(), account.getUsername(), LocalDateTime.now());

        kafkaTemplate.send("user-activity", event);
        return ResponseEntity.ok("User registered");
    }
}
