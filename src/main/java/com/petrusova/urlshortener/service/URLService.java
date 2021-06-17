package com.petrusova.urlshortener.service;

import com.petrusova.urlshortener.domain.Url;

import java.util.List;

public interface URLService {
    Url registerUrl(String longURL, Integer redirectType, String accountId);

    Url findUrlByShort(String shortURL);

    List<Url> findAllByAccountId(String accountId);

    void incrementCallsValue(Url url);
}
