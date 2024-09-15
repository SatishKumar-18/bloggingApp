package com.project.BloggingApp.service;

import com.project.BloggingApp.entity.Article;
import com.project.BloggingApp.entity.User;
import com.project.BloggingApp.repository.ArticleRepository;
import com.project.BloggingApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ArticleRepository articleRepo;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Boolean createUser(User user){
        try{
            if(!user.getUsername().isEmpty() && !user.getPassword().isEmpty()){
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepo.save(user);
                return true;
            }
            return false;
        } catch (Exception e){
            return false;
        }
    }

    public void save(User user){
        userRepo.save(user);
    }

    public User getUser(String username){
        return userRepo.findByUsername(username);
    }

    public String getUserProfile(User user){
        StringBuilder articleSlug= new StringBuilder();
        for(Article article : user.getArticle()){
            articleSlug.append(article.getSlug()).append(": ").append(article.getContent()).append("\n");
        }

        return user.getUsername() + "\n " + "Bio- " + user.getBio() + "\n "
                + "Articles- " + articleSlug;
    }

    public void deleteUser(String username){
        User user = userRepo.findByUsername(username);
        userRepo.delete(user);
        articleRepo.deleteByAuthor(username);
    }

}
