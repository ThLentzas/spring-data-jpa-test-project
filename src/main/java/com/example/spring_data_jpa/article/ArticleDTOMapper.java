package com.example.spring_data_jpa.article;

import java.util.function.Function;
import java.util.stream.Collectors;

import com.example.spring_data_jpa.topic.TopicDTOMapper;

import org.springframework.stereotype.Component;

/*
    Typically mappers are used in one class, the relative service, so they are not components. In this case they are
    also used by the topic service, so we have two instances and can make it a component.
 */
@Component
public class ArticleDTOMapper implements Function<Article, ArticleDTO> {
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
