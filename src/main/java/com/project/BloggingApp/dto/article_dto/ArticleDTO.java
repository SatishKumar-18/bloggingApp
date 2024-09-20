package com.project.BloggingApp.dto.article_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Article")
public class ArticleDTO {
    @NotEmpty
    private String title;
    private String subtitle;
    @NotEmpty
    private String content;
    private List<String> tags = new ArrayList<>();
}
