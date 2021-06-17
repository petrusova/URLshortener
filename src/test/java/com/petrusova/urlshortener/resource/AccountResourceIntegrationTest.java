package com.petrusova.urlshortener.resource;

import com.petrusova.urlshortener.UrLshortenerApplication;
import com.petrusova.urlshortener.repository.AccountRepository;
import com.petrusova.urlshortener.resource.dtos.AccountCreationRequest;
import com.petrusova.urlshortener.resource.dtos.AccountCreationResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UrLshortenerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountResourceIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @Before
    public void setUp() {
        accountRepository.deleteAll();
    }

    @Test
    public void createAccount() {
        // Given
        String accountId = "accountId";
        AccountCreationRequest request = new AccountCreationRequest();
        request.setAccountId(accountId);

        // When
        HttpEntity<AccountCreationRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<AccountCreationResponse> response = restTemplate.exchange(
                aUrl("/account"),
                HttpMethod.POST, entity, AccountCreationResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AccountCreationResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.isSuccess()).isTrue();
        assertThat(responseBody.getDescription()).isEqualTo("Your account is opened.");
        assertThat(responseBody.getPassword()).hasSize(8);
    }

    @Test
    public void createAccount_alreadyExists() {
        // Given
        String accountId = "accountId";
        AccountCreationRequest request = new AccountCreationRequest();
        request.setAccountId(accountId);

        // When
        HttpEntity<AccountCreationRequest> entity = new HttpEntity<>(request, headers);
        restTemplate.exchange(
                aUrl("/account"),
                HttpMethod.POST, entity, AccountCreationResponse.class);
        ResponseEntity<AccountCreationResponse> response = restTemplate.exchange(
                aUrl("/account"),
                HttpMethod.POST, entity, AccountCreationResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        AccountCreationResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.isSuccess()).isFalse();
        assertThat(responseBody.getDescription()).isEqualTo("Account for this id already exists.");
        assertThat(responseBody.getPassword()).isNull();
    }

    @Test
    public void createAccount_accountIdNotProvided() {
        // Given
        AccountCreationRequest request = new AccountCreationRequest();

        // When
        HttpEntity<AccountCreationRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<AccountCreationResponse> response = restTemplate.exchange(
                aUrl("/account"),
                HttpMethod.POST, entity, AccountCreationResponse.class);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        AccountCreationResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.isSuccess()).isFalse();
        assertThat(responseBody.getDescription()).isEqualTo("Account id needs to provided.");
        assertThat(responseBody.getPassword()).isNull();
    }

    @Test
    public void createAccount_accountIdProvidedButEmpty() {
        // Given
        AccountCreationRequest request = new AccountCreationRequest();
        request.setAccountId("");

        // When
        HttpEntity<AccountCreationRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<AccountCreationResponse> response = restTemplate.exchange(
                aUrl("/account"),
                HttpMethod.POST, entity, AccountCreationResponse.class);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        AccountCreationResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.isSuccess()).isFalse();
        assertThat(responseBody.getDescription()).isEqualTo("Account id needs to provided.");
        assertThat(responseBody.getPassword()).isNull();
    }

    private String aUrl(String mapping) {
        return "http://localhost:" + port + mapping;
    }
}