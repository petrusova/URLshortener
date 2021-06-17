package com.petrusova.urlshortener.resource;

import com.petrusova.urlshortener.domain.Url;
import com.petrusova.urlshortener.resource.dtos.UrlRegisterRequest;
import com.petrusova.urlshortener.resource.dtos.UrlRegisterResponse;
import com.petrusova.urlshortener.service.URLBusinessService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UrlResourceUnitTest {

    @Mock
    private URLBusinessService urlBusinessService;
    @InjectMocks
    private UrlResource urlResource;

    @Test
    public void registerURL() {
        // Given
        String longUrl = "longUrl";
        String shortUrl = "shortUrl";
        String accountId = "accountId";

        UrlRegisterRequest request = new UrlRegisterRequest();
        request.setUrl(longUrl);
        request.setRedirectType(301);

        Url url = new Url(longUrl, shortUrl, 301, accountId);
        when(urlBusinessService.registerUrl(longUrl, request.getRedirectType(), accountId)).thenReturn(url);

        Authentication authentication = new TestingAuthenticationToken(accountId, "password");

        // When
        ResponseEntity<UrlRegisterResponse> responseEntity = urlResource.registerURL(request, authentication);

        // Then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        UrlRegisterResponse registerResponse = responseEntity.getBody();
        assertThat(registerResponse).isNotNull();
        assertThat(registerResponse.getShortUrl()).isEqualTo(shortUrl);
    }

    @Test
    public void registerURL_emptyUrl() {
        // Given
        String accountId = "accountId";

        UrlRegisterRequest request = new UrlRegisterRequest();
        request.setRedirectType(301);

        Authentication authentication = new TestingAuthenticationToken(accountId, "password");

        // When
        ResponseEntity<UrlRegisterResponse> responseEntity = urlResource.registerURL(request, authentication);

        // Then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNull();
        verifyNoInteractions(urlBusinessService);
    }

    @Test
    public void registerURL_wrongRedirectType() {
        // Given
        String accountId = "accountId";
        String longUrl = "longUrl";

        UrlRegisterRequest request = new UrlRegisterRequest();
        request.setUrl(longUrl);
        request.setRedirectType(418);

        Authentication authentication = new TestingAuthenticationToken(accountId, "password");

        // When
        ResponseEntity<UrlRegisterResponse> responseEntity = urlResource.registerURL(request, authentication);

        // Then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNull();
        verifyNoInteractions(urlBusinessService);
    }

    @Test
    public void registerURL_entryAlreadyExists() {
        // Given
        String longUrl = "longUrl";
        String accountId = "accountId";

        UrlRegisterRequest request = new UrlRegisterRequest();
        request.setUrl(longUrl);
        request.setRedirectType(301);

        when(urlBusinessService.registerUrl(longUrl, request.getRedirectType(), accountId)).thenReturn(null);

        Authentication authentication = new TestingAuthenticationToken(accountId, "password");

        // When
        ResponseEntity<UrlRegisterResponse> responseEntity = urlResource.registerURL(request, authentication);

        // Then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNull();
    }
}