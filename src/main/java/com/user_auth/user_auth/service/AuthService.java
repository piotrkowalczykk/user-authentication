package com.user_auth.user_auth.service;

import com.user_auth.user_auth.dto.AuthRequestBody;
import com.user_auth.user_auth.dto.AuthResponseBody;
import com.user_auth.user_auth.model.AuthUser;

public interface AuthService {

    public AuthUser getUser(String email);
    public AuthResponseBody registerUser(AuthRequestBody authRequestBody);

}
