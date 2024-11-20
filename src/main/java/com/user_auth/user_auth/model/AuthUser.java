package com.user_auth.user_auth.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String email;
    private String password;

    public AuthUser(String email, String password){
        this.email = email;
        this.password = password;
    }

    public AuthUser(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
