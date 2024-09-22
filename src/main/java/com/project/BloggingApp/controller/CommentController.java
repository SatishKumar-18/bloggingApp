package com.project.BloggingApp.controller;

import com.project.BloggingApp.dto.comment_dto.CommentDTO;
import com.project.BloggingApp.entity.Article;
import com.project.BloggingApp.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles/{article-slug}/comments")
@Tag(name = "Comment APIs")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/add")
    public ResponseEntity<Article> addComment(@PathVariable("article-slug") String articleSlug, @RequestBody CommentDTO commentDTO){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(commentDTO != null){
            Article article = commentService.addComment(articleSlug, commentDTO, username);

            if(article != null){
                return new ResponseEntity<>(article, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("article-slug") String articleSlug, @PathVariable ObjectId id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        commentService.deleteComment(articleSlug, id, username);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
