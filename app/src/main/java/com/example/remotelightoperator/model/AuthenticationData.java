package com.example.remotelightoperator.model;

import java.util.Objects;

public class AuthenticationData {
    private String email;
    private String password;

    public String getPassword() {
        return password;
    }

    public AuthenticationData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticationData that = (AuthenticationData) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }
}
