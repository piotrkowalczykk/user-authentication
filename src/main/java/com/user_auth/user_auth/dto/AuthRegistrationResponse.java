package com.user_auth.user_auth.dto;

public class AuthRegistrationResponse {
    private final String token;
    private final String message;

    public AuthRegistrationResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }
}
