package online.shops.simple.initialize;

import online.shops.simple.models.Account;
import online.shops.simple.models.AccountRole;
import online.shops.simple.repositories.account.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final AccountRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(AccountRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String adminUsername = "admin1";
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            Account admin = new Account();
            admin.setUsername(adminUsername);
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRole(AccountRole.ADMIN);
            userRepository.save(admin);
            System.out.println("🛡️ Admin user created: " + adminUsername);
        }
    }
}