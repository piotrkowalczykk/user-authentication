package com.user_auth.user_auth.services;

import com.user_auth.user_auth.models.AuthUser;
import com.user_auth.user_auth.repositories.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthUserServiceImpl implements AuthUserService{

    @Autowired
    private AuthUserRepository authUserRepository;

    @Override
    public String getUserEmailById(Long id) {
        AuthUser authUser = authUserRepository.getFirstById(id);
        return authUser.getEmail();
    }
}
