package com.petrusova.urlshortener.service;

import com.petrusova.urlshortener.domain.Account;

public interface AccountService {

    String createAccount(String accountId);

    Account findAccount(String accountId);
}
