package com.Backend.SpringSecurity.AuthSecurity.service;

import com.Backend.SpringSecurity.AuthSecurity.dto.LoginRequest;
import com.Backend.SpringSecurity.AuthSecurity.dto.TokenResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

 private final AuthenticationManager authenticationManager;
 private final JWTService jwtService;
 private final RefreshTokenService refreshTokenService;
 private final long accessExpiration;

  public AuthService(
          AuthenticationManager authenticationManager,
          JWTService jwtService,
          RefreshTokenService refreshTokenService,
          @Value("${jwt.access-expiration}") long accessExpiration) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.accessExpiration = accessExpiration;
    }

    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                ));
        String accessToken = jwtService.generateAccessToken(request.getUsername());

        String refreshToken = refreshTokenService.createRefreshToken(
                        request.getUsername()
                );
        return new TokenResponse(
                accessToken,
                refreshToken,
                "Bearer",
                accessExpiration / 1000
        );
  }
  public TokenResponse refresh(String oldRefreshToken) {

        if (!refreshTokenService.isValid(oldRefreshToken)) {
            throw new BadCredentialsException(
                    "Invalid or expired refresh token"
            );
        }
        String username = refreshTokenService.getUsername(oldRefreshToken);

        refreshTokenService.revokeRefreshToken(oldRefreshToken);
        String newRefreshToken = refreshTokenService.createRefreshToken(username);

        String newAccessToken = jwtService.generateAccessToken(username);
        return new TokenResponse(
                newAccessToken,
                newRefreshToken,
                "Bearer",
                accessExpiration / 1000
        );
    }
    public void logout(String refreshToken) {
        refreshTokenService.revokeRefreshToken(refreshToken);
    }
}