package com.Backend.SpringSecurity.AuthSecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OtpRequest {
    String email;
    String otp;
}
