package com.Backend.SpringSecurity.AuthSecurity.service;

import com.Backend.SpringSecurity.AuthSecurity.model.RefreshToken;
import com.Backend.SpringSecurity.AuthSecurity.repository.RefreshTokenRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public RefreshTokenService(
            RefreshTokenRepository repository) {
        this.repository = repository;
    }

    public String createRefreshToken(String username) {

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUsername(username);
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken.setExpiryDate(
                Instant.now().plusMillis(refreshExpiration)
        );

        refreshToken.setRevoked(false);

        repository.save(refreshToken);

        return refreshToken.getToken();
    }

    public boolean isValid(String token) {

        return repository.findByToken(token)
                .filter(t -> !t.isRevoked())
                .filter(t ->
                        t.getExpiryDate().isAfter(Instant.now())
                )
                .isPresent();
    }

    public void revokeRefreshToken(String token) {

        repository.findByToken(token).ifPresent(t -> {
            t.setRevoked(true);
            repository.save(t);
        });
    }

    public String getUsername(String token) {

        return repository.findByToken(token)
                .filter(t -> !t.isRevoked())
                .filter(t -> t.getExpiryDate().isAfter(Instant.now()))
                .map(RefreshToken::getUsername)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid or expired refresh token"
                        ));
    }
}