package com.project.BloggingApp.service;

import com.project.BloggingApp.dto.article_dto.ArticleDTO;
import com.project.BloggingApp.entity.Article;
import com.project.BloggingApp.entity.User;
import com.project.BloggingApp.repository.ArticleRepository;
import com.project.BloggingApp.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public ArticleService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    private CommentRepository commentRepo;

    @Transactional
    public Article createArticle(ArticleDTO articleDTO, String username){
       try{
               Article article = modelMapper.map(articleDTO, Article.class);
               User user = userService.getUser(username);

               article.setPublishedDate(LocalDateTime.now());
               article.setAuthor(username);
               article.setSlug(article.getTitle().toLowerCase().replaceAll("\\s+", "-"));

               Article savedArticle = articleRepo.save(article);

               user.getArticle().add(savedArticle);
               userService.save(user);

               return savedArticle;
       }catch (Exception e){
           log.error("Exception occurred while saving article ", e);
           return null;
       }
    }

    public Article updateArticle(ObjectId id, ArticleDTO articleDTO){
        try{
            Article oldArticle = articleRepo.findById(id).orElseThrow();

            if(oldArticle != null){
                oldArticle.setTitle(articleDTO.getTitle());
                oldArticle.setSlug(articleDTO.getTitle().toLowerCase().replaceAll("\\s+", "-"));
                oldArticle.setSubtitle(articleDTO.getSubtitle());
                oldArticle.setContent(articleDTO.getContent());
                oldArticle.setTags(articleDTO.getTags());
            }

            return articleRepo.save(oldArticle);
        }catch (Exception e){
            log.error("Exception",e);
            return null;
        }
    }

    public void saveArticle(Article article){
        articleRepo.save(article);
    }

    public Article getArticleByTitle(String title){
        return articleRepo.findByTitle(title);
    }

    public Article getArticleBySlug(String slug){
        return articleRepo.findBySlug(slug);
    }

    public List<Article> getAllArticle(){
        return articleRepo.findAll();
    }

    public List<Article> getArticlesByAuthor(String author){

        return articleRepo.findAll().stream().filter(article -> article.getAuthor().equals(author)).toList();
    }

    public Boolean deleteArticle(String title, String username){
        boolean deleted = false;
        try{
            User user = userService.getUser(username);
            Article article = articleRepo.findByTitle(title);

            deleted = user.getArticle().removeIf(x -> x.getTitle().equals(title));


            if(deleted){
                commentRepo.deleteAll();
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
