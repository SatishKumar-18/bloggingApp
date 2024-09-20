package com.project.BloggingApp.controller;

import com.project.BloggingApp.dto.article_dto.ArticleDTO;
import com.project.BloggingApp.entity.Article;
import com.project.BloggingApp.entity.User;
import com.project.BloggingApp.service.ArticleService;
import com.project.BloggingApp.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
@Tag(name = "Article APIs", description = "(Authenticated) Create, update and delete article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserService userService;

    @PostMapping("/create-article")
    public ResponseEntity<Article> createArticle(@RequestBody ArticleDTO articleDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(articleDTO != null){
            Article article = articleService.createArticle(articleDTO, username);

            return new ResponseEntity<>(article, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/all-articles")
    public ResponseEntity<List<Article>> getAllArticles(){
        List<Article> articles = articleService.getAllArticle();

        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/search/{value}")
    public ResponseEntity<?> getArticlesByAuthor(@PathVariable String value){
        List<Article> articles = articleService.getArticlesByAuthor(value);
        Article article = articleService.getArticleByTitle(value);

        if(!articles.isEmpty()){
            return new ResponseEntity<>(articles, HttpStatus.OK);
        }
        if(article != null){
            return new ResponseEntity<>(article, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Article> updateArticle(@RequestBody ArticleDTO articleDTO, @PathVariable ObjectId id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.getUser(username);

        List<Article> list = user.getArticle().stream().filter(article -> article.getId().equals(id)).toList();

        if(!list.isEmpty()){
            Article article = articleService.saveArticle(id, articleDTO);
            if(article != null){
                return new ResponseEntity<>(article, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete/{title}")
    public ResponseEntity<?> deleteArticle(@PathVariable String title){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        articleService.deleteArticle(title, username);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}