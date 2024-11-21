package com.user_auth.user_auth.service;

import com.user_auth.user_auth.dto.AuthLoginRequest;
import com.user_auth.user_auth.dto.AuthLoginResponse;
import com.user_auth.user_auth.dto.AuthRegistrationRequest;
import com.user_auth.user_auth.dto.AuthRegistrationResponse;
import com.user_auth.user_auth.model.AuthUser;
import com.user_auth.user_auth.repository.AuthUserRepository;
import com.user_auth.user_auth.utils.Encoder;
import com.user_auth.user_auth.utils.JsonWebToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthUserRepository authUserRepository;
    private final Encoder encoder;
    private final JsonWebToken jwt;

    @Autowired
    AuthServiceImpl(AuthUserRepository authUserRepository, Encoder encoder, JsonWebToken jwt){
        this.authUserRepository = authUserRepository;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @Override
    public AuthUser getUser(String email) {
        return authUserRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("User not found"));
    }

    @Override
    public AuthRegistrationResponse registerUser(AuthRegistrationRequest authRegistrationRequest) {
        authUserRepository.save(new AuthUser(authRegistrationRequest.getEmail(), encoder.encode(authRegistrationRequest.getPassword())));
        return new AuthRegistrationResponse("token", "User registered successfully");
    }

    @Override
    public AuthLoginResponse loginUser(AuthLoginRequest authLoginRequest){
        AuthUser user = authUserRepository.findByEmail(authLoginRequest.getEmail()).orElseThrow(()-> new IllegalArgumentException("User not found"));
        if(!encoder.matches(authLoginRequest.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("Password is incorrect");
        }
        String token = jwt.generateToken(authLoginRequest.getEmail());
        return new AuthLoginResponse(token, "Successfully logged in");
    }
}
