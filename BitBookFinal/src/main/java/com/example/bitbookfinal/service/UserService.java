package com.example.bitbookfinal.service;

import com.example.bitbookfinal.model.User;
import com.example.bitbookfinal.repository.ReviewRepository;
import com.example.bitbookfinal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public void save(User user) {
        userRepository.save(user);
    }

    public boolean exists(User user){ //Comprobamos si existe o no el usuario con username y contraseña
        User user1 = userRepository.findByUsername(user.getUsername());
        if(user1!=null){
            return user1.getPassword().equals(user.getPassword());
        }else{
            return false;
        }
    }
}


