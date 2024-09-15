package com.project.BloggingApp.service;

import com.project.BloggingApp.entity.Comment;
import com.project.BloggingApp.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepo;
    @Autowired
    private ArticleService articleService;

}
