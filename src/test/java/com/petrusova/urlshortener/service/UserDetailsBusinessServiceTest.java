package com.petrusova.urlshortener.service;

import com.petrusova.urlshortener.domain.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsBusinessServiceTest {

    @Mock
    private AccountBusinessService accountBusinessService;
    @InjectMocks
    private UserDetailsBusinessService userDetailsBusinessService;

    @Test
    public void loadUserByUsername() {
        // Given
        String accountId = "accountId";
        Account account = new Account(accountId, "psw");
        when(accountBusinessService.findAccount(accountId)).thenReturn(account);

        // When
        UserDetails userDetails = userDetailsBusinessService.loadUserByUsername(accountId);

        // Then
        assertThat(userDetails.getUsername()).isEqualTo(accountId);
        assertThat(userDetails.getPassword()).isEqualTo("psw");
    }

    @Test
    public void loadUserByUsername_notFound() {
        // Given
        String accountId = "accountId";
        when(accountBusinessService.findAccount(accountId)).thenReturn(null);

        // When
        try {
            userDetailsBusinessService.loadUserByUsername(accountId);
            fail("Exception not thrown!");
        } catch (UsernameNotFoundException e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("Account with id 'accountId' not found");
        }
    }
}