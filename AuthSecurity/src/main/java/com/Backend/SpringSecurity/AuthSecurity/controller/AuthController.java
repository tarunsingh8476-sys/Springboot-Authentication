package com.Backend.SpringSecurity.AuthSecurity.controller;

import com.Backend.SpringSecurity.AuthSecurity.dto.LoginRequest;
import com.Backend.SpringSecurity.AuthSecurity.dto.OtpRequest;
import com.Backend.SpringSecurity.AuthSecurity.dto.RefreshRequest;
import com.Backend.SpringSecurity.AuthSecurity.dto.TokenResponse;
import com.Backend.SpringSecurity.AuthSecurity.model.User;
import com.Backend.SpringSecurity.AuthSecurity.service.AuthService;
import com.Backend.SpringSecurity.AuthSecurity.service.JWTService;
import com.Backend.SpringSecurity.AuthSecurity.service.OtpService;
import com.Backend.SpringSecurity.AuthSecurity.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final OtpService otpService;

    public AuthController(AuthService authService, UserService userService, OtpService otpService) {
        this.authService = authService;
        this.userService = userService;
        this.otpService = otpService;
    }

    @PostMapping("/register")
    public User registerUser( @RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String,String>> logout(@RequestBody RefreshRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(Map.of("message" ,"Logged out successfully"));
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequest request) {
        otpService.sendOtp(request.getOtp());
        return ResponseEntity.ok("OTP sent successfully");
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpRequest request) {
        otpService.verifyOtp(request.getEmail(),request.getOtp());
        return ResponseEntity.ok("OTP verified successfully");
    }

}
