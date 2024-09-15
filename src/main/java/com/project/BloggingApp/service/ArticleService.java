package com.project.BloggingApp.service;

import com.project.BloggingApp.entity.Article;
import com.project.BloggingApp.entity.User;
import com.project.BloggingApp.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepo;
    @Autowired
    private UserService userService;

    public void createArticle(Article article, String username){
       try{
           User user = userService.getUser(username);

           article.setPublishedDate(LocalDateTime.now());
           article.setAuthor(username);

           user.getArticle().add(article);
           articleRepo.save(article);
       }catch (Exception e){
           log.error("Exception occurred while saving article ", e);
       }
    }

    public void saveArticle(Article article){
        try{
            articleRepo.save(article);
        }catch (Exception e){
            log.error("Exception",e);
        }
    }

    public Article getArticleBySlug(String slug){
        return articleRepo.findBySlug(slug);
    }

    public List<Article> getAllArticle(){
        return articleRepo.findAll();
    }

    public Boolean deleteArticle(String slug, String username){
        boolean deleted = false;
        try{
            User user = userService.getUser(username);
            Article article = articleRepo.findBySlug(slug);

            deleted = user.getArticle().removeIf(x -> x.getSlug().equals(slug));

            if(deleted){
                articleRepo.delete(article);
                userService.save(user);
            }
            return deleted;
        } catch (Exception e){
            log.error("Exception ", e);
            throw new RuntimeException("An error occurred while deleting article");
        }

    }
}
