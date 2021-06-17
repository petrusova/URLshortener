package com.petrusova.urlshortener.service;

import com.petrusova.urlshortener.domain.Url;

public interface RedirectService {
    Url redirect(String shortUrl);
}
