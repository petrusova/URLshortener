package com.petrusova.urlshortener.service;

import com.petrusova.urlshortener.domain.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedirectBusinessService implements RedirectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedirectBusinessService.class);
    private final URLService urlService;

    @Autowired
    public RedirectBusinessService(URLService urlService) {
        this.urlService = urlService;
    }

    @Override
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
