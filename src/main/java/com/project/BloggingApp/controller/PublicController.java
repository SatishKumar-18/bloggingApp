package com.project.BloggingApp.controller;

import com.project.BloggingApp.entity.User;
import com.project.BloggingApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok("Working");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user){
        if(user != null){
            boolean saved  = userService.createUser(user);

            if(saved){
                return ResponseEntity.ok(user);
            }
        }

        return new ResponseEntity<>("Please submit valid data", HttpStatus.BAD_REQUEST);
    }
}
