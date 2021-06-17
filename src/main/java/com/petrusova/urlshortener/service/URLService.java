package com.petrusova.urlshortener.service;

import com.petrusova.urlshortener.domain.Url;
import com.petrusova.urlshortener.repository.URLRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class URLService {

    private final URLRepository urlRepository;

    @Autowired
    public URLService(URLRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Url registerUrl(String longURL, Integer redirectType, String accountId) {
        Optional<Url> existingURL = urlRepository.findByLongURL(longURL);
        if (existingURL.isPresent()) {
            return null;
        }
        String shortURL = RandomStringUtils.randomAlphabetic(6);
        existingURL = urlRepository.findByShortURL(shortURL);
        if (existingURL.isPresent()) {
            return null;
        }

        Url url = new Url(longURL, shortURL, redirectType, accountId);
        return urlRepository.save(url);
    }

    public Url findUrlByShort(String shortURL) {
        Optional<Url> url = urlRepository.findByShortURL(shortURL);
        return url.orElse(null);
    }

    public Url findUrlByLong(String longURL) {
        Optional<Url> url = urlRepository.findByLongURL(longURL);
        return url.orElse(null);

    }

    public List<Url> findAllByAccountId(String accountId) {
        return urlRepository.findAllByAccountId(accountId);
    }

    public void incrementCallsValue(Url url) {
        url.incrementCalls();
        urlRepository.save(url);
    }
}
