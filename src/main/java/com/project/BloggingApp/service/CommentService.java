package com.project.BloggingApp.service;

import com.project.BloggingApp.dto.comment_dto.CommentDTO;
import com.project.BloggingApp.entity.Article;
import com.project.BloggingApp.entity.Comment;
import com.project.BloggingApp.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentService {
    @Autowired
    private CommentRepository commentRepo;
    @Autowired
    private ArticleService articleService;

    private final ModelMapper modelMapper;

    public CommentService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Article addComment(String slug, CommentDTO commentDTO, String username){
        try{
            Comment comment = modelMapper.map(commentDTO, Comment.class);
            Article article= articleService.getArticleBySlug(slug);

            comment.setUser(username);
            comment.setCommentTime(LocalDateTime.now());

            Comment savedComment = commentRepo.save(comment);
            article.getComments().add(savedComment);
            articleService.saveArticle(article);

            return article;

        }catch (Exception e){
            log.error("Exception occurred while adding comment ", e);
            return null;
        }
    }

    public void deleteComment(String slug, ObjectId id, String username){

        boolean deleted = false;
        try{
            Article article = articleService.getArticleBySlug(slug);

            String author = article.getAuthor();
            Comment comment = commentRepo.findById(id).orElseThrow();
            String user = comment.getUser();

            if(username.equals(author) || username.equals(user)) {
                deleted = article.getComments().removeIf(x -> x.getId().equals(id));

                if (deleted) {
                    commentRepo.deleteById(id);
                    articleService.saveArticle(article);
                }
            }

        }catch (Exception e){
            log.error("Exception ", e);
            throw new RuntimeException("An error occurred while deleting comment");
        }

    }

}
