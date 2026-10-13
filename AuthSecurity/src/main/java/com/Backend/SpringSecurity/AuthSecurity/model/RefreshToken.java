package com.Backend.SpringSecurity.AuthSecurity.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
@Table(indexes = @Index(columnList = "token" , unique = true))
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true)
    private String token;

    private Instant expiryDate;
    private boolean revoked;
}
