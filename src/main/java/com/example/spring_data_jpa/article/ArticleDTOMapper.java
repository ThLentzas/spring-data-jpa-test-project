package com.example.spring_data_jpa.article;

import java.util.function.Function;

class ArticleDTOMapper implements Function<Article, ArticleDTO> {

    @Override
    public ArticleDTO apply(Article article) {
        return new ArticleDTO(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getStatus(),
                article.getCreatedDate(),
                article.getPublishedDate(),
                article.getTopics(),
                article.getComments()
        );
    }
}
