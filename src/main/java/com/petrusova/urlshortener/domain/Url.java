package com.petrusova.urlshortener.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Url {

    @Id
    @GeneratedValue
    int id;
    String longURL;
    String shortURL;
    int redirectType;
    int calls;
    String accountId;

    protected Url() {
    }

    public Url(String longURL, String shortURL, Integer redirectType, String accountId) {
        this.longURL = longURL;
        this.shortURL = shortURL;
        this.redirectType = redirectType == null ? 301 : redirectType;
        this.accountId = accountId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLongURL() {
        return longURL;
    }

    public void setLongURL(String longURL) {
        this.longURL = longURL;
    }

    public String getShortURL() {
        return shortURL;
    }

    public void setShortURL(String shortURL) {
        this.shortURL = shortURL;
    }

    public int getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(int redirectType) {
        this.redirectType = redirectType;
    }

    public int getCalls() {
        return calls;
    }

    public void setCalls(int calls) {
        this.calls = calls;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void incrementCalls() {
        this.calls++;
    }
}
