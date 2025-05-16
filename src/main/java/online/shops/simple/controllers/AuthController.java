package online.shops.simple.controllers;

import com.nimbusds.jose.JOSEException;
import lombok.AllArgsConstructor;
import lombok.Data;
import online.shops.simple.config.JwtService;
import online.shops.simple.models.Account;
import online.shops.simple.models.AccountRole;
import online.shops.simple.repositories.account.AccountRepository;
import online.shops.simple.services.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
    private final AuthenticationManager authManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager, UserDetailsServiceImpl userDetailsService,
                          AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) throws JOSEException {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String token = JwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest registerRequest) {
        Optional<Account> optionalAccount = accountRepository.findByUsername(registerRequest.username);
        if (optionalAccount.isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        Account account = new Account();
        account.setUsername(registerRequest.getUsername());
        account.setRole(AccountRole.USER);
        account.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        accountRepository.save(account);
        return ResponseEntity.ok("User registered");
    }
}
