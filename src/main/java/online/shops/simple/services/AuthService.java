package online.shops.simple.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;

import online.shops.simple.config.JwtService;
import online.shops.simple.models.Account;
import online.shops.simple.models.AccountRole;
import online.shops.simple.repositories.account.AccountRepository;

@Service
public class AuthService {
    private final AuthenticationManager authManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authManager, 
                      UserDetailsServiceImpl userDetailsService,
                      AccountRepository accountRepository, 
                      PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String username, String password) throws JOSEException {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        UserDetails user = userDetailsService.loadUserByUsername(username);
        return JwtService.generateToken(user.getUsername());
    }

    public boolean isUsernameExists(String username) {
        return accountRepository.findByUsername(username).isPresent();
    }

    public Account registerUser(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setRole(AccountRole.USER);
        account.setPasswordHash(passwordEncoder.encode(password));
        return accountRepository.save(account);
    }
}
