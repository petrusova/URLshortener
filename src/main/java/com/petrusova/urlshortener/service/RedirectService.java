package com.petrusova.urlshortener.service;

import com.petrusova.urlshortener.domain.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedirectService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedirectService.class);
    private final URLService urlService;

    @Autowired
    public RedirectService(URLService urlService) {
        this.urlService = urlService;
    }

    public Url redirect(String shortUrl) {
        Url url = urlService.findUrlByShort(shortUrl);
        if (url == null) {
            LOGGER.warn("URL '{}' not registered.", shortUrl);
            return null;
        }
        urlService.incrementCallsValue(url);
        return url;
    }
}
