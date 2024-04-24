package com.example.bitbookfinal.service;

import com.example.bitbookfinal.model.User;
import com.example.bitbookfinal.repository.ReviewRepository;
import com.example.bitbookfinal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService  {
    @Autowired
    private UserRepository userRepository;


    public void save(User user) {
        userRepository.save(user);
    }



}


