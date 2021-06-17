package com.petrusova.urlshortener.resource;

import com.petrusova.urlshortener.UrLshortenerApplication;
import com.petrusova.urlshortener.domain.Url;
import com.petrusova.urlshortener.repository.URLRepository;
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
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UrLshortenerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedirectResourceIntegrationTest {

    private String shortUrl;
    private String longUrl;

    @Autowired
    private URLRepository urlRepository;

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @Before
    public void setUp() {
        urlRepository.deleteAll();
        shortUrl = RandomStringUtils.randomAlphabetic(6);
        longUrl = aUrl("/help");
        Url url = new Url(longUrl, shortUrl, 301, "accountId");
        urlRepository.save(url);
    }

    @Test
    public void redirect() {
        // When
        HttpEntity<Object> entity = new HttpEntity<>(null, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                aUrl("/" + shortUrl),
                HttpMethod.GET, entity, Void.class);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private String aUrl(String mapping) {
        return "http://localhost:" + port + mapping;
    }
}