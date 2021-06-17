package com.petrusova.urlshortener.service;

import com.petrusova.urlshortener.domain.Url;
import com.petrusova.urlshortener.repository.URLRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class URLBusinessServiceTest {

    @Mock
    private URLRepository urlRepository;
    @Captor
    private ArgumentCaptor<Url> captor;
    @InjectMocks
    private URLBusinessService urlBusinessService;

    @Test
    public void registerUrl() {
        // Given
        String accountId = "accountId";
        String longUrl = "long";
        when(urlRepository.findByLongURL(longUrl)).thenReturn(Optional.empty());
        when(urlRepository.findByShortURL(anyString())).thenReturn(Optional.empty());
        when(urlRepository.save(any(Url.class))).thenAnswer(returnsFirstArg());

        // When
        Url registeredUrl = urlBusinessService.registerUrl(longUrl, 302, accountId);

        // Then
        assertThat(registeredUrl).isNotNull();
        assertThat(registeredUrl.getShortURL()).hasSize(6);
        assertThat(registeredUrl.getLongURL()).isEqualTo(longUrl);
        assertThat(registeredUrl.getAccountId()).isEqualTo(accountId);
        assertThat(registeredUrl.getRedirectType()).isEqualTo(302);
    }

    @Test
    public void registerUrl_longUrlAlreadyExists() {
        // Given
        String accountId = "accountId";
        String longUrl = "long";
        Url url = aUrl(accountId, "1");
        when(urlRepository.findByLongURL(longUrl)).thenReturn(Optional.of(url));

        // When
        Url registeredUrl = urlBusinessService.registerUrl(longUrl, 302, accountId);

        // Then
        assertThat(registeredUrl).isNull();
        verify(urlRepository).findByLongURL(longUrl);
        verifyNoMoreInteractions(urlRepository);
    }

    @Test
    public void registerUrl_shortUrlAlreadyExists() {
        // Given
        String accountId = "accountId";
        Url url = aUrl(accountId, "1");
        String longUrl = url.getLongURL();

        when(urlRepository.findByLongURL(longUrl)).thenReturn(Optional.empty());
        when(urlRepository.findByShortURL(anyString())).thenReturn(Optional.of(url));

        // When
        Url registeredUrl = urlBusinessService.registerUrl(longUrl, 302, accountId);

        // Then
        assertThat(registeredUrl).isNull();
        verify(urlRepository).findByLongURL(longUrl);
        verify(urlRepository).findByShortURL(anyString());
        verifyNoMoreInteractions(urlRepository);
    }

    @Test
    public void findUrlByShort() {
        // Given
        String accountId = "accountId";
        Url url = aUrl(accountId, "1");
        String shortUrl = url.getShortURL();
        when(urlRepository.findByShortURL(shortUrl)).thenReturn(Optional.of(url));

        // When
        Url foundUrl = urlBusinessService.findUrlByShort(shortUrl);
        // Then
        assertThat(foundUrl).isNotNull().isEqualTo(url);
    }

    @Test
    public void findUrlByShort_notFound() {
        // Given
        String shortUrl = "short";
        when(urlRepository.findByShortURL(shortUrl)).thenReturn(Optional.empty());

        // When
        Url foundUrl = urlBusinessService.findUrlByShort(shortUrl);
        // Then
        assertThat(foundUrl).isNull();
    }

    @Test
    public void findAllByAccountId() {
        // Given
        String accountId = "accountId";
        String shortUrl = "short";
        Url url1 = aUrl(accountId, "1");
        Url url2 = aUrl(accountId, "2");
        Url url3 = aUrl(accountId, "3");
        when(urlRepository.findAllByAccountId(accountId)).thenReturn(asList(url1, url2, url3));

        // When
        List<Url> foundUrls = urlBusinessService.findAllByAccountId(accountId);
        // Then

        assertThat(foundUrls).hasSize(3).containsExactlyInAnyOrder(url1, url2, url3);
    }

    @Test
    public void incrementCallsValue() {
        // Given
        String accountId = "accountId";
        Url url = aUrl(accountId, "1");
        url.setCalls(8);

        // When
        urlBusinessService.incrementCallsValue(url);

        // Then
        verify(urlRepository).save(captor.capture());
        Url capturedUrl = captor.getValue();
        assertThat(capturedUrl.getCalls()).isEqualTo(9);
    }

    private Url aUrl(String accountId, String suffix) {
        return new Url("longUrl" + suffix, "shortUrl" + suffix, 301, accountId);
    }
}