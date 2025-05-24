package online.shops.simple.initialize;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import online.shops.simple.models.Account;
import online.shops.simple.models.AccountRole;
import online.shops.simple.repositories.account.AccountRepository;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final AccountRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Value("${app.admin.username}")
    private String adminUsername;
    
    @Value("${app.admin.password}")
    private String adminPassword;

    public AdminInitializer(AccountRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            Account admin = new Account();
            admin.setUsername(adminUsername);
            admin.setPasswordHash(passwordEncoder.encode(adminPassword));
            admin.setRole(AccountRole.ADMIN);
            userRepository.save(admin);
        }
    }
}