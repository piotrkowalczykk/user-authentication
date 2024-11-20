package com.user_auth.user_auth.service;

import com.user_auth.user_auth.dto.AuthRequestBody;
import com.user_auth.user_auth.dto.AuthResponseBody;
import com.user_auth.user_auth.model.AuthUser;
import com.user_auth.user_auth.repository.AuthUserRepository;
import com.user_auth.user_auth.utils.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthUserRepository authUserRepository;
    private final Encoder encoder;

    @Autowired
    AuthServiceImpl(AuthUserRepository authUserRepository, Encoder encoder){
        this.authUserRepository = authUserRepository;
        this.encoder = encoder;
    }

    @Override
    public AuthUser getUser(String email) {
        return authUserRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("User not found"));
    }

    @Override
    public AuthResponseBody registerUser(AuthRequestBody authRequestBody) {
        authUserRepository.save(new AuthUser(authRequestBody.getEmail(), encoder.encode(authRequestBody.getPassword())));
        return new AuthResponseBody("token", "User registered successfully");
    }
}
