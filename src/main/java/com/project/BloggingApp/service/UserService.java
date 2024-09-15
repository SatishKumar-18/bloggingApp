package com.project.BloggingApp.service;

import com.project.BloggingApp.entity.User;
import com.project.BloggingApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    public Boolean createUser(User user){
        if(!user.getUsername().isEmpty() && !user.getPassword().isEmpty()){
            userRepo.save(user);
            return true;
        }
        return false;
    }

    public void save(User user){
        userRepo.save(user);
    }

    public User getUser(String username){
        return userRepo.findByUsername(username);
    }

    public void deleteUser(String username){
        User user = userRepo.findByUsername(username);

        userRepo.delete(user);
    }
}
