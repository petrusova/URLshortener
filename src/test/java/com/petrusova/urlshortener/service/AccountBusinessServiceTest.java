package com.petrusova.urlshortener.service;

import com.petrusova.urlshortener.domain.Account;
import com.petrusova.urlshortener.repository.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountBusinessServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Captor
    private ArgumentCaptor<Account> captor;

    @InjectMocks
    private AccountBusinessService accountBusinessService;

    @Test
    public void createAccount() {
        // Given
        String accountId = "myAccountId";
        String hashedPassword = "hashedPassword";

        when(accountRepository.existsById(accountId)).thenReturn(false);
        when(accountRepository.save(captor.capture())).thenAnswer(returnsFirstArg());
        when(passwordEncoder.encode(anyString())).thenReturn(hashedPassword);

        // When
        String createdPassword = accountBusinessService.createAccount(accountId);

        // Then
        assertThat(createdPassword).isNotNull().hasSize(8);

        Account createdAccount = captor.getValue();
        assertThat(createdAccount).isNotNull();
        assertThat(createdAccount.getId()).isEqualTo(accountId);
        assertThat(createdAccount.getUsername()).isEqualTo(accountId);
        assertThat(createdAccount.getPassword()).isEqualTo(hashedPassword);
        assertThat(createdAccount.isAccountNonExpired()).isTrue();
        assertThat(createdAccount.isAccountNonLocked()).isTrue();
        assertThat(createdAccount.isEnabled()).isTrue();
        assertThat(createdAccount.isCredentialsNonExpired()).isTrue();
    }

    @Test
    public void createAccount_alreadyExists() {
        // Given
        String accountId = "myAccountId";
        when(accountRepository.existsById(accountId)).thenReturn(true);

        // When
        String createdAccountPassword = accountBusinessService.createAccount(accountId);

        // Then
        assertThat(createdAccountPassword).isNull();
    }

    @Test
    public void findAccount() {
        // Given
        String accountId = "myAccountId";
        Account account = new Account(accountId, "pwd");
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // When
        Account foundAccount = accountBusinessService.findAccount(accountId);

        // Then
        assertThat(foundAccount).isEqualTo(account);
    }

    @Test
    public void findAccount_notFound() {
        // Given
        String accountId = "myAccountId";
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // When
        Account account = accountBusinessService.findAccount(accountId);

        // Then
        assertThat(account).isNull();
    }
}