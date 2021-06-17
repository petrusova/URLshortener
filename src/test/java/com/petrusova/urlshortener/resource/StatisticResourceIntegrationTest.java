package com.petrusova.urlshortener.resource;

import com.petrusova.urlshortener.UrLshortenerApplication;
import com.petrusova.urlshortener.domain.Url;
import com.petrusova.urlshortener.repository.AccountRepository;
import com.petrusova.urlshortener.repository.URLRepository;
import com.petrusova.urlshortener.service.AccountService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UrLshortenerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatisticResourceIntegrationTest {

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
        Url url = new Url(aUrl("/help"), RandomStringUtils.randomAlphabetic(6), 302, accountId);
        url.setCalls(9);
        urlRepository.save(url);
    }

    @Test
    public void getStatistics() {
        // When
        HttpEntity<Object> entity = new HttpEntity<>(null, headers);
        ResponseEntity<Map<String, Integer>> response = restTemplate.exchange(
                aUrl("/statistic/" + accountId),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                });

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<String, Integer> statistics = response.getBody();
        assertThat(statistics).isNotNull().hasSize(1).containsEntry(aUrl("/help"), 9);
    }

    private String aUrl(String mapping) {
        return "http://localhost:" + port + mapping;
    }
}