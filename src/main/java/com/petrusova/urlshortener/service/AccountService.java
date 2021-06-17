package com.petrusova.urlshortener.service;

import com.petrusova.urlshortener.domain.Account;

import java.util.Optional;

public interface AccountService {

    String createAccount(String accountId);

    Optional<Account> findAccount(String accountId);
}
