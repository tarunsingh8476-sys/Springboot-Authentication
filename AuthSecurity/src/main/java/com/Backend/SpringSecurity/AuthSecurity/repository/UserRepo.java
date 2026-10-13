package com.Backend.SpringSecurity.AuthSecurity.repository;

import com.Backend.SpringSecurity.AuthSecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepo  extends JpaRepository<User,Integer> {

    public User  findByUsername(String username);
}
