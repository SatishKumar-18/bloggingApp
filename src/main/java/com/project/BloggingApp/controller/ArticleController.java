package com.project.BloggingApp.controller;

import com.project.BloggingApp.entity.Article;
import com.project.BloggingApp.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/create-article")
    public ResponseEntity<Article> createArticle(@RequestBody Article article){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(article != null){
            articleService.createArticle(article, username);

            return new ResponseEntity<>(article, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete/{slug}")
    public ResponseEntity<?> deleteArticle(@PathVariable String slug){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        articleService.deleteArticle(slug, username);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}