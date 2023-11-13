package com.example.spring_data_jpa.article;

import com.example.spring_data_jpa.exception.DuplicateResourceException;
import com.example.spring_data_jpa.topic.Topic;
import com.example.spring_data_jpa.topic.TopicRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
class ArticleService {
    private final ArticleRepository articleRepository;
    private final TopicRepository topicRepository;
    private static final ArticleDTOMapper mapper = new ArticleDTOMapper();

    public ArticleDTO createArticle(ArticleCreateRequest createRequest) {
        if(articleRepository.existsByTitle(createRequest.title())) {
            throw new DuplicateResourceException("Article already exists with title: " + createRequest.title());
        }

        Set<Topic> topics = new HashSet<>();
        for (Topic topic : createRequest.topics()) {
            topicRepository.findByName(topic.getName()).ifPresentOrElse(
                    topics::add,
                    () -> {
                        topicRepository.save(topic);
                        topics.add(topic);
                    });
        }

        Article article = new Article(createRequest.title(), createRequest.content(), topics);
        article = articleRepository.save(article);

        return mapper.apply(article);
    }
}
