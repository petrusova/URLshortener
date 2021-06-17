package com.petrusova.urlshortener.resource;

import com.petrusova.urlshortener.domain.Url;
import com.petrusova.urlshortener.resource.dtos.UrlRegisterRequest;
import com.petrusova.urlshortener.resource.dtos.UrlRegisterResponse;
import com.petrusova.urlshortener.service.URLService;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class UrlResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlResource.class);
    private final URLService urlService;

    @Autowired
    public UrlResource(URLService urlService) {
        this.urlService = urlService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<UrlRegisterResponse> registerURL(@RequestBody UrlRegisterRequest request,
                                                           Authentication authentication) {
        boolean requestValid = validateRequest(request);
        if (!requestValid) {
            return ResponseEntity.badRequest().build();
        }
        String accountId = authentication.getName();
        Url registeredUrl = urlService.registerUrl(request.getUrl(), request.getRedirectType(), accountId);

        if (registeredUrl == null) {
            LOGGER.warn("URL '{}' not registered, entry already exists.", request.getUrl());
            return ResponseEntity.badRequest().build();
        }

        LOGGER.info("Registered URL '{}' -> '{}'", registeredUrl.getLongURL(), registeredUrl.getShortURL());
        UrlRegisterResponse response = new UrlRegisterResponse(registeredUrl.getShortURL());
        return ResponseEntity.ok().body(response);
    }

    private boolean validateRequest(UrlRegisterRequest request) {
        return request != null && Strings.isNotEmpty(request.getUrl()) && (request.getRedirectType() == 0 || request.getRedirectType() == 301 || request.getRedirectType() == 302);
    }
}
