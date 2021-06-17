package com.petrusova.urlshortener.repository;

import com.petrusova.urlshortener.domain.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, String> {
}
