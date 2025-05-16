package org.smartlearnai.model.auth;

import org.smartlearnai.model.dto.UserResponseDto;

public class AuthResponse {
    private boolean success;
    private String message;
    private UserResponseDto user;
    private String token;

    public AuthResponse() {
    }

    public AuthResponse(boolean success, String message, UserResponseDto user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    // Constructor for error response
    public AuthResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.user = null;
    }

    // Constructor with token
    public AuthResponse(boolean success, String message, UserResponseDto user, String token) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserResponseDto getUser() {
        return user;
    }

    public void setUser(UserResponseDto user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
