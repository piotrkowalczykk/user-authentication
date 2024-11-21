package com.user_auth.user_auth.service;

import com.user_auth.user_auth.dto.AuthLoginRequest;
import com.user_auth.user_auth.dto.AuthLoginResponse;
import com.user_auth.user_auth.dto.AuthRegistrationRequest;
import com.user_auth.user_auth.dto.AuthRegistrationResponse;
import com.user_auth.user_auth.model.AuthUser;

public interface AuthService {

    public AuthUser getUser(String email);
    public AuthRegistrationResponse registerUser(AuthRegistrationRequest authRegistrationRequest);
    public AuthLoginResponse loginUser(AuthLoginRequest authLoginRequest);
}
