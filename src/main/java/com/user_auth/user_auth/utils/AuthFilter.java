package com.user_auth.user_auth.utils;

import com.user_auth.user_auth.model.AuthUser;
import com.user_auth.user_auth.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthFilter extends HttpFilter {

    private final List<String> unsecuredEndpoints = Arrays.asList(
            "/api/v1/authentication/login",
            "/api/v1/authentication/register"
    );

    private final JsonWebToken jwt;
    private final AuthService authService;

    public AuthFilter(JsonWebToken jwt, AuthService authService){
        this.jwt = jwt;
        this.authService = authService;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if("OPTIONS".equalsIgnoreCase(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String path = request.getRequestURI();

        if(unsecuredEndpoints.contains(path)){
            chain.doFilter(request, response);
            return;
        }

        try {
            String authorization = request.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer")){
                throw  new ServerException("Token missing");
            }

            String token = authorization.substring(7);

            if (jwt.isTokenExpired(token)) {
                throw new ServletException("Invalid token");
            }

            String email = jwt.getEmailFromToken(token);
            AuthUser user = authService.getUser(email);
            request.setAttribute("authenticatedUser", user);
            chain.doFilter(request, response);

        } catch (Exception e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Invalid authentication token, or token missing.\"}");
        }
    }
}
