package com.petrusova.urlshortener.resource.dtos;

public class AccountCreationResponse {
    private boolean success;
    private String description;
    private String password;

    public AccountCreationResponse(boolean success, String description, String password) {
        this.success = success;
        this.description = description;
        this.password = password;
    }

    public AccountCreationResponse(boolean success, String description) {
        this.success = success;
        this.description = description;
    }

    public AccountCreationResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
