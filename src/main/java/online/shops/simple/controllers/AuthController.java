package online.shops.simple.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;

import lombok.AllArgsConstructor;
import lombok.Data;
import online.shops.simple.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Data
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

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) throws JOSEException {
        String token = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest registerRequest) {
        if (authService.isUsernameExists(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        
        authService.registerUser(registerRequest.getUsername(), registerRequest.getPassword());
        return ResponseEntity.ok("User registered");
    }
}
