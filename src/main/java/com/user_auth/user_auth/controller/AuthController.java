package com.user_auth.user_auth.controller;

import com.user_auth.user_auth.dto.AuthLoginRequest;
import com.user_auth.user_auth.dto.AuthLoginResponse;
import com.user_auth.user_auth.dto.AuthRegistrationRequest;
import com.user_auth.user_auth.dto.AuthRegistrationResponse;
import com.user_auth.user_auth.model.AuthUser;
import com.user_auth.user_auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/register")
    public AuthRegistrationResponse registerUser(@Valid @RequestBody AuthRegistrationRequest authRegistrationRequest){
        return authService.registerUser(authRegistrationRequest);
    }

    @PostMapping("/login")
    public AuthLoginResponse registerUser(@Valid @RequestBody AuthLoginRequest authLoginRequest){
        return authService.loginUser(authLoginRequest);
    }

}
