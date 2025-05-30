package online.shops.simple.services;

import org.springframework.stereotype.Service;

import online.shops.simple.models.Account;
import online.shops.simple.repositories.account.AccountRepository;

@Service
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    public Long getAccountIdByUsername(String username) {
        return accountRepository.findByUsername(username)
                .map(Account::getId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }
} 