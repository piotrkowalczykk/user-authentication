package com.user_auth.user_auth.service;

import com.user_auth.user_auth.dto.AuthLoginRequest;
import com.user_auth.user_auth.dto.AuthLoginResponse;
import com.user_auth.user_auth.dto.AuthRegistrationRequest;
import com.user_auth.user_auth.dto.AuthRegistrationResponse;
import com.user_auth.user_auth.model.AuthUser;
import com.user_auth.user_auth.repository.AuthUserRepository;
import com.user_auth.user_auth.utils.EmailService;
import com.user_auth.user_auth.utils.Encoder;
import com.user_auth.user_auth.utils.JsonWebToken;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthUserRepository authUserRepository;
    private final Encoder encoder;
    private final JsonWebToken jwt;
    private final EmailService emailService;
    private final int durationInMinutes = 1;

    @Autowired
    AuthServiceImpl(AuthUserRepository authUserRepository, Encoder encoder, JsonWebToken jwt, EmailService emailService){
        this.authUserRepository = authUserRepository;
        this.encoder = encoder;
        this.jwt = jwt;
        this.emailService = emailService;
    }

    @Override
    public AuthUser getUser(String email) {
        return authUserRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("User not found"));
    }

    @Override
    public AuthRegistrationResponse registerUser(AuthRegistrationRequest authRegistrationRequest) throws MessagingException, UnsupportedEncodingException {
        String emailVerificationToken = generateEmailVerificationToken();
        String hashedToken = encoder.encode(emailVerificationToken);

        AuthUser user = new AuthUser();
        user.setEmail(authRegistrationRequest.getEmail());
        user.setPassword(encoder.encode(authRegistrationRequest.getPassword()));
        user.setEmailVerificationToken(hashedToken);
        user.setEmailVerificationTokenExpiryDate(LocalDateTime.now().plusMinutes(durationInMinutes));
        authUserRepository.save(user);

        String subject = "Email Verification";
        String content = String.format("Enter this code to verify your email: %s. The code will expire in %s minutes.", emailVerificationToken, durationInMinutes);

        try{
            emailService.sendEmail(authRegistrationRequest.getEmail(), subject, content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String token = jwt.generateToken(authRegistrationRequest.getEmail());
        return new AuthRegistrationResponse(token, "User registered successfully");
    }

    @Override
    public void validateEmailVerificationToken(String emailToken, String email){
        Optional<AuthUser> user = authUserRepository.findByEmail(email);
        if(user.isPresent() && encoder.matches(emailToken, user.get().getEmailVerificationToken()) && !user.get().getEmailVerificationTokenExpiryDate().isBefore(LocalDateTime.now())){
            user.get().setEmailVerified(true);
            user.get().setEmailVerificationToken(null);
            user.get().setEmailVerificationTokenExpiryDate(null);
            authUserRepository.save(user.get());
        } else if(user.isPresent() && encoder.matches(emailToken, user.get().getEmailVerificationToken()) && !user.get().getEmailVerificationTokenExpiryDate().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Email verification token expired.");
        } else {
            throw new IllegalArgumentException("Email verification token failed.");
        }
    }

    @Override
    public void sendEmailVerificationToken(String email){
        Optional<AuthUser> user = authUserRepository.findByEmail(email);
        if(user.isPresent() && !user.get().getEmailVerified()){
            String emailVerificationToken = generateEmailVerificationToken();
            String hashedToken = encoder.encode(emailVerificationToken);

            user.get().setEmailVerificationToken(hashedToken);
            user.get().setEmailVerificationTokenExpiryDate(LocalDateTime.now().plusMinutes(durationInMinutes));
            authUserRepository.save(user.get());

            String subject = "Email Verification";
            String content = String.format("Enter this code to verify your email: %s. The code will expire in %s minutes.", emailVerificationToken, durationInMinutes);

            try{
                emailService.sendEmail(email, subject, content);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Email verification token failed, or email is already verified");
        }
    }

    @Override
    public void sendPasswordResetToken(String email){
        Optional<AuthUser> user = authUserRepository.findByEmail(email);

        if(user.isPresent()) {
            String passwordResetToken = generateEmailVerificationToken();
            String hashedToken = encoder.encode(generateEmailVerificationToken());

            user.get().setPasswordResetToken(hashedToken);
            user.get().setPasswordResetTokenExpiryDate(LocalDateTime.now().plusMinutes(durationInMinutes));
            authUserRepository.save(user.get());

            String subject = "Password Reset";
            String content = String.format("You requested a password reset. Enter this code to reset your password: %s. The code will expire in %s minutes.", passwordResetToken, durationInMinutes);

            try{
                emailService.sendEmail(email, subject, content);
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
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

    @Override
    public String generateEmailVerificationToken() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 5; i++){
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
