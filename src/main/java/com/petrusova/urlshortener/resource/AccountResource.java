package com.petrusova.urlshortener.resource;

import com.petrusova.urlshortener.resource.dtos.AccountCreationRequest;
import com.petrusova.urlshortener.resource.dtos.AccountCreationResponse;
import com.petrusova.urlshortener.service.AccountService;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountResource.class);
    private final AccountService accountService;

    @Autowired
    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<AccountCreationResponse> createAnAccount(@RequestBody AccountCreationRequest request) {
        if (request == null || Strings.isEmpty(request.getAccountId())) {
            LOGGER.warn("Account id needs to be provided");
            AccountCreationResponse response = new AccountCreationResponse(false, "Account id needs to provided.");
            return ResponseEntity.badRequest().body(response);
        }

        String createdAccountPassword = accountService.createAccount(request.getAccountId());

        if (createdAccountPassword == null) {
            LOGGER.warn("Account not created for id: '{}', it already exists.", request.getAccountId());
            AccountCreationResponse response = new AccountCreationResponse(false, "Account for this id already exists.");
            return ResponseEntity.badRequest().body(response);
        }
        LOGGER.info("Account created for id: '{}'", request.getAccountId());
        AccountCreationResponse response = new AccountCreationResponse(true, "Your account is opened.", createdAccountPassword);
        return ResponseEntity.ok().body(response);
    }
}