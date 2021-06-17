package com.petrusova.urlshortener.service;

import com.petrusova.urlshortener.domain.Account;
import com.petrusova.urlshortener.repository.AccountRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountBusinessService implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountBusinessService(AccountRepository accountRepository,
                                  PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String createAccount(String accountId) {
        if (accountRepository.existsById(accountId)) {
            return null;
        }
        String password = RandomStringUtils.randomAlphanumeric(8);
        String hashedPw = passwordEncoder.encode(password);
        accountRepository.save(new Account(accountId, hashedPw));
        return password;
    }

    @Override
    public Optional<Account> findAccount(String accountId) {
        return accountRepository.findById(accountId);
    }
}
