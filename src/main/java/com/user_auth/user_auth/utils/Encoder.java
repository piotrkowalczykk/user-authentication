package com.user_auth.user_auth.utils;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class Encoder {
    public String encode(String rawPassword){
        try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(rawPassword.getBytes());
        return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error encoding password");
        }
    }

    public boolean matches(String rawPassword, String encodedPassword){
        return encode(rawPassword).equals(encodedPassword);
    }
}
