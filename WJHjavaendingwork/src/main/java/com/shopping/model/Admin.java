package com.shopping.model;

import java.sql.Timestamp;

public class Admin {
    private int id;
    private String username;
    private String password;
    private Timestamp lastLogin;
    private String loginHistory;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLoginHistory() {
        return loginHistory;
    }

    public void setLoginHistory(String loginHistory) {
        this.loginHistory = loginHistory;
    }
}
