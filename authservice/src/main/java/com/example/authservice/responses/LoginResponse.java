package com.example.authservice.responses;

public class LoginResponse {
    private String token;
    private long expiresIn;
    private String userId;
    private Boolean isUserInfoInitialized;

    public String getToken() {
        return token;
    }

    // Constructor mặc định
    public LoginResponse() {}

    // Constructor với tham số
    public LoginResponse(String token, long expiresIn, String userId) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.userId = userId;
    }

    // Constructor với tham số đầy đủ
    public LoginResponse(String token, long expiresIn, String userId, Boolean isUserInfoInitialized) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.userId = userId;
        this.isUserInfoInitialized = isUserInfoInitialized;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public String getUserId() {
        return userId;
    }

    public Boolean getIsUserInfoInitialized() {
        return isUserInfoInitialized;
    }

    // Setters with fluent interface
    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public LoginResponse setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public LoginResponse setIsUserInfoInitialized(Boolean isUserInfoInitialized) {
        this.isUserInfoInitialized = isUserInfoInitialized;
        return this;
    }
}
