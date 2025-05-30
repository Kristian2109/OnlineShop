package online.shops.simple.mappers;

import online.shops.simple.dtos.AccountDto;
import online.shops.simple.models.Account;

public class AccountMapper {
    public static AccountDto toDto(Account account) {
        return new AccountDto(account.getId(), account.getUsername(), account.getCreatedAt());
    }
}