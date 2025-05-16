package online.shops.simple.repositories.account;

import online.shops.simple.models.Account;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaAccountRepository implements AccountRepository {

    private final SpringDataAccountRepository springRepo;

    public JpaAccountRepository(SpringDataAccountRepository springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Optional<Account> findByUsername(String username) {
        return springRepo.findByUsername(username);
    }

    @Override
    public Account save(Account account) {
        return springRepo.save(account);
    }

    @Override
    public Optional<Account> findById(Long id) {
        return springRepo.findById(id);
    }
}
