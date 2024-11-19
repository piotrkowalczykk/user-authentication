package com.user_auth.user_auth.controller;

import com.user_auth.user_auth.model.AuthUser;
import com.user_auth.user_auth.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authentication")
public class AuthController{

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @GetMapping("/user")
    public AuthUser getUser(){
        return authService.getUser("test@gmail.com");
    }

}
