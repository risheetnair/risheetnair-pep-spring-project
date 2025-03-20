package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account register(String username, String password) {
        if (username.isBlank()) {
            return null;
        }

        if (this.accountRepository.existsByUsername(username)) {
            return null;
        }

        if (password.length() < 4) {
            return null;
        }

        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);

        return this.accountRepository.save(account);
    }

    public Account login(String username, String password) {
        if (this.accountRepository.existsByUsername(username)) {
            Account account = this.accountRepository.findByUsername(username);

            if (account.getPassword().equals(password)) {
                return account;
            }
        }

        return null;
    }
}
