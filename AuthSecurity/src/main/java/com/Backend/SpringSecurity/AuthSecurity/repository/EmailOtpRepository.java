package com.Backend.SpringSecurity.AuthSecurity.repository;

import com.Backend.SpringSecurity.AuthSecurity.model.EmailOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailOtpRepository  extends JpaRepository<EmailOtp,Long> {

    Optional<EmailOtp> findByEmail(String email);

}
