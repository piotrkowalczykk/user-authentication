package com.user_auth.user_auth.service;

import com.user_auth.user_auth.model.AuthUser;

public interface AuthService {

    public AuthUser getUser(String email);

}
