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
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;


    public void save(User user) {
        userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user1 = userRepository.findByUsername(username); //Te devuelve un usuario dado un username
        //Convertimos user al formato UserDetails
        if(user1!=null){
            List<GrantedAuthority> listaroles = new ArrayList<>();
            for(String rol: user1.getRoles()){
                listaroles.add(new SimpleGrantedAuthority(rol));
            }
            UserDetails new_user = new org.springframework.security.core.userdetails.User(user1.getUsername(),
                    user1.getPassword(),listaroles);
            return new_user;
        }
        throw new UsernameNotFoundException("Username not found");
    }
}


