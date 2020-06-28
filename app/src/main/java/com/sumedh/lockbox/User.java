package com.sumedh.lockbox;

public class User {
    private String username;
    private String email;
    private String userId;

    public String getUsername() {
        return username;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return String.format("<User: username: " + username + " | email: " + email + ">");
    }

    User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
