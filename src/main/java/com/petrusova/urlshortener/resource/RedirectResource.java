package com.petrusova.urlshortener.resource;

import com.petrusova.urlshortener.domain.Url;
import com.petrusova.urlshortener.service.RedirectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

@Controller
@RequestMapping(produces = "application/json")
public class RedirectResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedirectResource.class);
    private final RedirectService redirectService;

    @Autowired
    public RedirectResource(RedirectService redirectService) {
        this.redirectService = redirectService;
    }

    @GetMapping("/{shortUrl:[a-zA-Z]{6}}")
    public ResponseEntity<Void> redirect(@PathVariable String shortUrl) {
        LOGGER.info("Redirecting '{}' → ", shortUrl);
        Url url = redirectService.redirect(shortUrl);
        LOGGER.info(" → '{}'", url.getLongURL());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url.getLongURL()));
        return new ResponseEntity<>(headers, HttpStatus.valueOf(url.getRedirectType()));
    }
}
