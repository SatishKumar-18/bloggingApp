package com.project.BloggingApp.service;

import com.project.BloggingApp.entity.Article;
import com.project.BloggingApp.entity.User;
import com.project.BloggingApp.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepo;
    @Autowired
    private UserService userService;

    @Transactional
    public void createArticle(Article article, String username){
       try{
           boolean validArticle = !article.getTitle().isEmpty() && !article.getContent().isEmpty() && !article.getSlug().isEmpty();
           if(validArticle){
               User user = userService.getUser(username);

               article.setPublishedDate(LocalDateTime.now());
               article.setAuthor(username);

               Article savedArticle = articleRepo.save(article);

               user.getArticle().add(savedArticle);
               userService.save(user);
           }else{
               throw new RuntimeException("Invalid article data");
           }
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

    public void deleteArticle(String slug, String username){
        boolean deleted = false;
        try{
            User user = userService.getUser(username);
            Article article = articleRepo.findBySlug(slug);

            deleted = user.getArticle().removeIf(x -> x.getSlug().equals(slug));

            if(deleted){
                articleRepo.delete(article);
                userService.save(user);
            }
        } catch (Exception e){
            log.error("Exception ", e);
            throw new RuntimeException("An error occurred while deleting article");
        }
    }
}
