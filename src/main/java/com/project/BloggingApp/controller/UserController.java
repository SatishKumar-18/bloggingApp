package com.project.BloggingApp.controller;

import com.project.BloggingApp.dto.user_dto.UpdateUserDTO;
import com.project.BloggingApp.entity.User;
import com.project.BloggingApp.service.ArticleService;
import com.project.BloggingApp.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "User APIs", description = "(Authenticated) Update, search, view profile and delete user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ArticleService articleService;

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDTO updateUserDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();


        User updatedUser = userService.updateUser(updateUserDTO, username);
        if(updatedUser != null){
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }

       return new ResponseEntity<>("Error", HttpStatus.EXPECTATION_FAILED);
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
