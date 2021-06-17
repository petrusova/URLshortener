package com.petrusova.urlshortener.resource;

import com.petrusova.urlshortener.domain.Url;
import com.petrusova.urlshortener.service.RedirectBusinessService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RedirectResourceUnitTest {

    @Mock
    private RedirectBusinessService redirectBusinessService;
    @InjectMocks
    private RedirectResource redirectResource;

    @Test
    public void redirect() {
        // Given
        String shortUrl = "shortUrl";
        String longUrl = "longUrl";
        Url url = new Url(longUrl, shortUrl, 301, "accountId");
        when(redirectBusinessService.redirect(shortUrl)).thenReturn(url);

        // When
        ResponseEntity<Void> response = redirectResource.redirect(shortUrl);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.MOVED_PERMANENTLY);
        HttpHeaders responseHeaders = response.getHeaders();
        assertThat(responseHeaders).isNotEmpty();
        assertThat(responseHeaders.getLocation()).hasToString(longUrl);
    }
}