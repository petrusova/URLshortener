package com.petrusova.urlshortener.service;

import com.petrusova.urlshortener.domain.Account;

public interface AccountService {

    Account createAccount(String accountId);

    Account findAccount(String accountId);
}
