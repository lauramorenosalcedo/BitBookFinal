package com.example.bitbookfinal.service;

import com.example.bitbookfinal.model.Category;
import com.example.bitbookfinal.model.User;
import com.example.bitbookfinal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService  {
    @Autowired
    private UserRepository userRepository;
    public void save(User user) {
        userRepository.save(user);
    }


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }


    public Optional<User> findByUsername(String username) {
        return  userRepository.findByUsername(username);
    }

    public void editEmail(User user, String email) {
        user.setEmail(email);
        userRepository.save(user);

    }

    public void deleteUser(User user){
        Long id1 = user.getId();
        deleteById(id1);
    }
}


