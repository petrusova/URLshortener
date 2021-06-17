package com.petrusova.urlshortener.service;

import com.petrusova.urlshortener.domain.Url;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RedirectBusinessServiceTest {
    @Mock
    private URLService urlService;

    @InjectMocks
    private RedirectBusinessService redirectBusinessService;

    @Test
    public void redirect() {
        // Given
        String shortUrl = "shortUrl";
        String accountId = "accountId";
        Url url = new Url("longUrl", shortUrl, 301, accountId);
        when(urlService.findUrlByShort(shortUrl)).thenReturn(url);

        // When
        Url foundUrl = redirectBusinessService.redirect(shortUrl);

        // Then
        assertThat(foundUrl).isNotNull().isEqualTo(foundUrl);
        verify(urlService).incrementCallsValue(url);
    }

    @Test
    public void redirect_notFound() {
        // Given
        String shortUrl = "shortUrl";
        when(urlService.findUrlByShort(shortUrl)).thenReturn(null);

        // When
        Url foundUrl = redirectBusinessService.redirect(shortUrl);

        // Then
        assertThat(foundUrl).isNull();
    }
}