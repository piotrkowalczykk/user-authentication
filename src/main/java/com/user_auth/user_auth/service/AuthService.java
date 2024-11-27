package com.user_auth.user_auth.service;

import com.user_auth.user_auth.dto.AuthLoginRequest;
import com.user_auth.user_auth.dto.AuthLoginResponse;
import com.user_auth.user_auth.dto.AuthRegistrationRequest;
import com.user_auth.user_auth.dto.AuthRegistrationResponse;
import com.user_auth.user_auth.model.AuthUser;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface AuthService {

    public AuthUser getUser(String email);
    public AuthRegistrationResponse registerUser(AuthRegistrationRequest authRegistrationRequest) throws MessagingException, UnsupportedEncodingException;
    public AuthLoginResponse loginUser(AuthLoginRequest authLoginRequest);
    public String generateEmailVerificationToken();
    public void validateEmailVerificationToken(String emailToken, String email);
    public void sendEmailVerificationToken(String email);
    public void sendPasswordResetToken(String email);
    public void resetPassword(String email, String newPassword, String token);


}
