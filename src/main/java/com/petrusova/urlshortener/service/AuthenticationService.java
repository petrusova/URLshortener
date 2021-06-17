package com.petrusova.urlshortener.service;

import com.petrusova.urlshortener.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

    private final AccountService accountService;

    @Autowired
    public AuthenticationService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = accountService.findAccount(username);
        if (account.isEmpty()) {
            throw new UsernameNotFoundException("Account with id '" + username + "' not found");
        } else {
            return account.get();
        }
    }
}
