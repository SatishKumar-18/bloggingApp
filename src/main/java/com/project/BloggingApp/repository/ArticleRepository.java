package com.project.BloggingApp.repository;

import com.project.BloggingApp.entity.Article;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ArticleRepository extends MongoRepository<Article, ObjectId> {

    Article findByTitle(String title);
    Article findBySlug(String slug);
    void deleteByAuthor(String author);
}
