package com.petrusova.urlshortener.service;

import com.petrusova.urlshortener.domain.Account;
import com.petrusova.urlshortener.domain.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsService.class);

    private final AccountService accountService;
    private final URLService urlService;

    @Autowired
    public StatisticsService(AccountService accountService,
                             URLService urlService) {
        this.accountService = accountService;
        this.urlService = urlService;
    }

    public Map<String, Integer> getStatistics(String accountId) {
        Optional<Account> account = accountService.findAccount(accountId);
        if (account.isEmpty()) {
            LOGGER.warn("Trying to retrieve statistics for nonexistent account id '{}'.", accountId);
            return Collections.emptyMap();
        }
        List<Url> urlsForAccount = urlService.findAllByAccountId(accountId);
        if (urlsForAccount.isEmpty()) {
            LOGGER.warn("No URLs registered for account id '{}'.", accountId);
            return Collections.emptyMap();
        }
        return urlsForAccount.stream().collect(Collectors.toMap(Url::getLongURL, Url::getCalls));
    }
}
