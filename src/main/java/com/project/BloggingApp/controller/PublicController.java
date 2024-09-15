package com.project.BloggingApp.controller;

import com.project.BloggingApp.entity.Article;
import com.project.BloggingApp.entity.User;
import com.project.BloggingApp.security.jwt.JwtUtils;
import com.project.BloggingApp.service.ArticleService;
import com.project.BloggingApp.service.UserDetailsServiceImpl;
import com.project.BloggingApp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    private UserService userService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String jwt = jwtUtils.generateToken(userDetails.getUsername());

            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }catch (Exception e){
            log.error("Exception occurred while creating AuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all-articles")
    public ResponseEntity<List<Article>> getArticleByUsername(){
        List<Article> articles = articleService.getAllArticle();

        return new ResponseEntity<>(articles, HttpStatus.OK);
    }
}
