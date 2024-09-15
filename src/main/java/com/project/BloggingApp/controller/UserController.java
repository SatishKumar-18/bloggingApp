package com.project.BloggingApp.controller;

import com.project.BloggingApp.entity.User;
import com.project.BloggingApp.service.ArticleService;
import com.project.BloggingApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ArticleService articleService;

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateUser(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User existingUser = userService.getUser(username);
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());

        userService.createUser(existingUser);

        return new ResponseEntity<>(existingUser, HttpStatus.OK);
    }

    @GetMapping("/search/{username}")
    public ResponseEntity<?> searchUserByUsername(@PathVariable String username){

        User user = userService.getUser(username);

        if(user != null){
            String profile = userService.getUserProfile(user);
            return new ResponseEntity<>(profile, HttpStatus.FOUND);
        }

        return new ResponseEntity<>("User Not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.getUser(username);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        userService.deleteUser(username);

        return new ResponseEntity<>("Account deleted", HttpStatus.OK);
    }



}
