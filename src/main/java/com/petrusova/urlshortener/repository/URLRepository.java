package com.petrusova.urlshortener.repository;

import com.petrusova.urlshortener.domain.Url;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface URLRepository extends CrudRepository<Url, String> {

    Optional<Url> findByShortURL(String shortURL);

    Optional<Url> findByLongURL(String longURL);

    List<Url> findAllByAccountId(String accountId);
}
