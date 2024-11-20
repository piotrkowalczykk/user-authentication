package com.user_auth.user_auth.controller;

import com.user_auth.user_auth.dto.AuthRequestBody;
import com.user_auth.user_auth.dto.AuthResponseBody;
import com.user_auth.user_auth.model.AuthUser;
import com.user_auth.user_auth.service.AuthService;
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
    public AuthResponseBody registerUser(@RequestBody AuthRequestBody authRequestBody){
        return authService.registerUser(authRequestBody);
    }

}
