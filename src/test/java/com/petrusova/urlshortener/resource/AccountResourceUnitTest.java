package com.petrusova.urlshortener.resource;

import com.petrusova.urlshortener.resource.dtos.AccountCreationRequest;
import com.petrusova.urlshortener.resource.dtos.AccountCreationResponse;
import com.petrusova.urlshortener.service.AccountBusinessService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountResourceUnitTest {

    @Mock
    private AccountBusinessService accountBusinessService;
    @InjectMocks
    private AccountResource accountResource;

    @Test
    public void createAnAccount() {
        // Given
        String accountId = "accountId";
        AccountCreationRequest request = new AccountCreationRequest();
        request.setAccountId(accountId);
        when(accountBusinessService.createAccount(accountId)).thenReturn("password");

        // When
        ResponseEntity<AccountCreationResponse> response = accountResource.createAnAccount(request);
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AccountCreationResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.isSuccess()).isTrue();
        assertThat(responseBody.getDescription()).isEqualTo("Your account is opened.");
        assertThat(responseBody.getPassword()).isEqualTo("password");
    }

    @Test
    public void createAnAccount_alreadyExists() {
        // Given
        String accountId = "accountId";
        AccountCreationRequest request = new AccountCreationRequest();
        request.setAccountId(accountId);
        when(accountBusinessService.createAccount(accountId)).thenReturn(null);

        // When
        ResponseEntity<AccountCreationResponse> response = accountResource.createAnAccount(request);
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        AccountCreationResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.isSuccess()).isFalse();
        assertThat(responseBody.getDescription()).isEqualTo("Account for this id already exists.");
        assertThat(responseBody.getPassword()).isNull();
    }

    @Test
    public void createAnAccount_accountIdNotProvided() {
        // Given
        AccountCreationRequest request = new AccountCreationRequest();

        // When
        ResponseEntity<AccountCreationResponse> response = accountResource.createAnAccount(request);
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        AccountCreationResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.isSuccess()).isFalse();
        assertThat(responseBody.getDescription()).isEqualTo("Account id needs to provided.");
        assertThat(responseBody.getPassword()).isNull();
        verifyNoInteractions(accountBusinessService);
    }

    @Test
    public void createAnAccount_accountIdProvidedButEmpty() {
        // Given
        AccountCreationRequest request = new AccountCreationRequest();
        request.setAccountId("");

        // When
        ResponseEntity<AccountCreationResponse> response = accountResource.createAnAccount(request);
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        AccountCreationResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.isSuccess()).isFalse();
        assertThat(responseBody.getDescription()).isEqualTo("Account id needs to provided.");
        assertThat(responseBody.getPassword()).isNull();
        verifyNoInteractions(accountBusinessService);
    }
}