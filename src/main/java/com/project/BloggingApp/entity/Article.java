package com.project.BloggingApp.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "articles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id
    private ObjectId id;
    @NonNull
    private String title;
    @NonNull
    @Indexed(unique = true)
    private String slug;
    private String subtitle;
    @NonNull
    private String content;
    private LocalDateTime publishedDate;
    private List<String> tags = new ArrayList<>();
    @NonNull
    private String author;

    @DBRef
    private List<Comment> comments = new ArrayList<>();

}
