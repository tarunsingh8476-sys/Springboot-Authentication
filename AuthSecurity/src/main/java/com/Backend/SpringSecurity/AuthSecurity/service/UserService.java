package com.Backend.SpringSecurity.AuthSecurity.service;

import com.Backend.SpringSecurity.AuthSecurity.repository.UserRepo;
import com.Backend.SpringSecurity.AuthSecurity.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo,PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }


    public User registerUser( User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
       return  userRepo.save(user);

    }

}
