package com.petrusova.urlshortener.service;

import com.petrusova.urlshortener.domain.Account;
import com.petrusova.urlshortener.domain.Url;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsBusinessServiceTest {

    @Mock
    private AccountService accountService;
    @Mock
    private URLService urlService;

    @InjectMocks
    private StatisticsBusinessService statisticsBusinessService;

    @Test
    public void getStatistics() {
        // Given
        String accountId = "accountId";
        Account account = new Account(accountId, "password");
        when(accountService.findAccount(accountId)).thenReturn(account);
        Url url = new Url("long", "short", 301, accountId);
        url.setCalls(5);
        when(urlService.findAllByAccountId(accountId)).thenReturn(singletonList(url));

        // When
        Map<String, Integer> statistics = statisticsBusinessService.getStatistics(accountId);

        // Then
        assertThat(statistics).isNotNull().hasSize(1).containsEntry("long", 5);
    }

    @Test
    public void getStatistics_accountMissing() {
        // Given
        String accountId = "accountId";
        when(accountService.findAccount(accountId)).thenReturn(null);

        // When
        Map<String, Integer> statistics = statisticsBusinessService.getStatistics(accountId);

        // Then
        assertThat(statistics).isEmpty();
    }

    @Test
    public void getStatistics_noUrls() {
        // Given
        String accountId = "accountId";
        Account account = new Account(accountId, "password");
        when(accountService.findAccount(accountId)).thenReturn(account);
        when(urlService.findAllByAccountId(accountId)).thenReturn(emptyList());

        // When
        Map<String, Integer> statistics = statisticsBusinessService.getStatistics(accountId);

        // Then
        assertThat(statistics).isEmpty();
    }
}