package com.petrusova.urlshortener.resource.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UrlRegisterRequest {

    @JsonProperty("URL")
    private String url;
    private Integer redirectType;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(Integer redirectType) {
        this.redirectType = redirectType;
    }
}
