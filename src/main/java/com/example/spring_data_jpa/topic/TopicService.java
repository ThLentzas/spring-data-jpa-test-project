package com.example.spring_data_jpa.topic;

import com.example.spring_data_jpa.article.Article;
import com.example.spring_data_jpa.article.ArticleDTO;
import com.example.spring_data_jpa.article.ArticleDTOMapper;
import com.example.spring_data_jpa.article.ArticleRepository;
import com.example.spring_data_jpa.exception.DuplicateResourceException;
import com.example.spring_data_jpa.exception.ResourceNotFoundException;
import com.example.spring_data_jpa.exception.StatusConflictException;

import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
class TopicService {
    private final TopicRepository topicRepository;
    private final ArticleRepository articleRepository;
    private static final TopicDTOMapper topicDTOMapper = new TopicDTOMapper();
    private static final ArticleDTOMapper articleDTOMapper = new ArticleDTOMapper();
    private static final String TOPIC_NOT_FOUND_ERROR_MSG = "Topic was not found with id: ";

    TopicDTO createTopic(TopicCreateRequest createRequest) {
        if(this.topicRepository.existsByNameIgnoringCase(createRequest.name())) {
            throw new DuplicateResourceException("Topic with name already exists: " + createRequest.name());
        }

        Topic topic = new Topic(createRequest.name());
        this.topicRepository.save(topic);

        return topicDTOMapper.apply(topic);
    }

    void updateTopic(Long id, TopicUpdateRequest topicUpdateRequest) {
        Topic topic = this.topicRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException(
                TOPIC_NOT_FOUND_ERROR_MSG + id));

        if(topic.getStatus().equals(TopicStatus.APPROVED)) {
            throw new StatusConflictException("Topic is already approved and can not be modified");
        }

        if(topicUpdateRequest.name() == null || topicUpdateRequest.name().isBlank()) {
            throw new IllegalArgumentException("You must provide a name to update the topic");
        }

        if(this.topicRepository.existsByNameIgnoringCase(topicUpdateRequest.name())) {
            throw new DuplicateResourceException("The provided topic name already exists");
        }

        topic.setName(topicUpdateRequest.name());
        this.topicRepository.save(topic);
    }

    void updateTopicStatus(Long id, String action) {
        Topic topic = this.topicRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException(
                TOPIC_NOT_FOUND_ERROR_MSG + id));

        if(topic.getStatus().equals(TopicStatus.APPROVED)) {
            throw new StatusConflictException("Topic is already approved and can not be modified");
        }

        if(!action.isBlank() && action.equalsIgnoreCase("approve")) {
            topic.setStatus(TopicStatus.APPROVED);
            this.topicRepository.save(topic);
        }

        if(!action.isBlank() && action.equalsIgnoreCase("reject")) {
            this.topicRepository.delete(topic);
        }

        //Here some other value than approve or reject was provided, so we should handle with some exception
    }

    /*
        Same output using streams

        return this.topicRepository.findById(id)
                .map(topicDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException(TOPIC_NOT_FOUND_ERROR_MSG + id));
     */
    TopicDTO findById(Long id) {
        Topic topic = this.topicRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException(
                TOPIC_NOT_FOUND_ERROR_MSG + id));

        return topicDTOMapper.apply(topic);
    }

    List<TopicDTO> findTopicsByName(String name) {
        List<Topic> topics = this.topicRepository.findAllByNameContainingIgnoreCaseOrderByCreatedDateDesc(name);

        return topics.stream()
                .map(topicDTOMapper)
                .toList();
    }

    /*
        Same output using streams. Same logic for the methods below.

         return this.topicRepository.findAllOrderByStatusAndCreatedDateDesc().stream()
            .map(topicDTOMapper)
            .toList();
     */
    List<TopicDTO> findAllTopics() {
        List<Topic> topics = this.topicRepository.findAllOrderByStatusAndCreatedDateDesc();

        return topics.stream()
                .map(topicDTOMapper)
                .toList();
    }

    List<ArticleDTO> findArticlesByTopicId(Long topicId) {
        List<Article> articles = this.articleRepository.findAllByTopicsId(topicId);

        return articles.stream()
                .map(articleDTOMapper)
                .toList();
    }

    /*
        Finding the topic by name and then using the topic's id to find all the associated articles. If the topic is not
        or articles for the specific topic are not found we return an empty list.
     */
    List<ArticleDTO> findArticlesByTopicName(String name) {
        if(name.isBlank()) {
            throw new IllegalArgumentException("You must provide a topic name");
        }

        return topicRepository.findByNameIgnoreCase(name)
                .map(topic -> articleRepository.findAllByTopicsId(topic.getId()))
                .orElse(Collections.emptyList())
                .stream()
                .map(articleDTOMapper)
                .toList();
    }
}
