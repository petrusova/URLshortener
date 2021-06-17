package com.petrusova.urlshortener.resource;

import com.petrusova.urlshortener.UrLshortenerApplication;
import com.petrusova.urlshortener.domain.Url;
import com.petrusova.urlshortener.repository.AccountRepository;
import com.petrusova.urlshortener.repository.URLRepository;
import com.petrusova.urlshortener.resource.dtos.AccountCreationRequest;
import com.petrusova.urlshortener.resource.dtos.AccountCreationResponse;
import com.petrusova.urlshortener.resource.dtos.UrlRegisterRequest;
import com.petrusova.urlshortener.resource.dtos.UrlRegisterResponse;
import com.petrusova.urlshortener.service.AccountService;
import org.apache.commons.lang3.RandomStringUtils;
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
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UrLshortenerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlResourceIntegrationTest {

    private final String accountId = "accountId";

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private URLRepository urlRepository;

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @Before
    public void setUp() {
        accountRepository.deleteAll();
        String password = accountService.createAccount(accountId);
        headers.setBasicAuth(accountId, password);

        urlRepository.deleteAll();
        Url url = new Url(aUrl("/existingUrl"), RandomStringUtils.randomAlphabetic(6), 301, accountId);
        urlRepository.save(url);
    }

    @Test
    public void registerURL() {
        // Given
        UrlRegisterRequest request = new UrlRegisterRequest();
        request.setUrl(aUrl("/test"));
        request.setRedirectType(301);

        // When
        HttpEntity<UrlRegisterRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<UrlRegisterResponse> responseEntity = restTemplate.exchange(
                aUrl("/register"),
                HttpMethod.POST, entity, UrlRegisterResponse.class);

        // Then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        UrlRegisterResponse registerResponse = responseEntity.getBody();
        assertThat(registerResponse).isNotNull();
        assertThat(registerResponse.getShortUrl()).hasSize(6);
    }

    @Test
    public void registerURL_emptyUrl() {
        // Given
        UrlRegisterRequest request = new UrlRegisterRequest();
        request.setRedirectType(301);

        // When
        HttpEntity<UrlRegisterRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<UrlRegisterResponse> responseEntity = restTemplate.exchange(
                aUrl("/register"),
                HttpMethod.POST, entity, UrlRegisterResponse.class);

        // Then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNull();
    }


    @Test
    public void registerURL_wrongRedirectType() {
        // Given

        UrlRegisterRequest request = new UrlRegisterRequest();
        request.setUrl("longUrl");
        request.setRedirectType(418);

        // When
        HttpEntity<UrlRegisterRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<UrlRegisterResponse> responseEntity = restTemplate.exchange(
                aUrl("/register"),
                HttpMethod.POST, entity, UrlRegisterResponse.class);

        // Then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void registerURL_entryAlreadyExists() {
        // Given
        UrlRegisterRequest request = new UrlRegisterRequest();
        request.setUrl(aUrl("/existingUrl"));
        request.setRedirectType(301);

        // When
        HttpEntity<UrlRegisterRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<UrlRegisterResponse> responseEntity = restTemplate.exchange(
                aUrl("/register"),
                HttpMethod.POST, entity, UrlRegisterResponse.class);

        // Then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNull();
    }

    private String aUrl(String mapping) {
        return "http://localhost:" + port + mapping;
    }
}