package online.shops.simple.repositories.account;

import online.shops.simple.models.Account;

import java.util.Optional;

public interface AccountRepository {

    Optional<Account> findByUsername(String username);

    Account save(Account account);

    Optional<Account> findById(Long id);
}
