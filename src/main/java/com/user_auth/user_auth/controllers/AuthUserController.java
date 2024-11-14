package com.user_auth.user_auth.controllers;

import com.user_auth.user_auth.services.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/authentication")
public class AuthUserController {

    @Autowired
    private AuthUserService authUserService;

    @GetMapping("/user/{id}")
    public ResponseEntity<String> getUserEmail(@PathVariable Long id){
        String name = authUserService.getUserEmailById(id);
        return new ResponseEntity<>(name, HttpStatus.OK);
    }

}
