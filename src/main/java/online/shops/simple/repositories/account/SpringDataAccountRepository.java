package online.shops.simple.repositories.account;

import online.shops.simple.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataAccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);
}