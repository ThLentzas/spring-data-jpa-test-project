package com.example.spring_data_jpa.article;

import com.example.spring_data_jpa.topic.TopicDTOMapper;

import java.util.function.Function;
import java.util.stream.Collectors;


class ArticleDTOMapper implements Function<Article, ArticleDTO> {
    private static final TopicDTOMapper topicDTOmapper = new TopicDTOMapper();

    @Override
    public ArticleDTO apply(Article article) {
        return new ArticleDTO(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getStatus(),
                article.getCreatedDate(),
                article.getPublishedDate(),
                article.getTopics().stream()
                        .map(topicDTOmapper)
                        .collect(Collectors.toSet())
        );
    }
}
