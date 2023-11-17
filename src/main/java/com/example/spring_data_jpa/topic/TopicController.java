package com.example.spring_data_jpa.topic;

import com.example.spring_data_jpa.article.ArticleDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/topics")
class TopicController {
    private final TopicService topicService;

    @PostMapping
    ResponseEntity<TopicDTO> createTopic(@RequestBody TopicCreateRequest topicCreateRequest) {
        TopicDTO topicDTO = this.topicService.createTopic(topicCreateRequest);

        return new ResponseEntity<>(topicDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> updateTopic(@PathVariable Long id, @RequestBody TopicUpdateRequest topicUpdateRequest) {
        this.topicService.updateTopic(id, topicUpdateRequest);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/status")
    ResponseEntity<Void> updateTopicStatus(@PathVariable Long id,
                                           @RequestParam(value = "action", defaultValue = "")
                                           String action) {
        this.topicService.updateTopicStatus(id, action);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    ResponseEntity<TopicDTO> findById(@PathVariable Long id) {
        TopicDTO topic = this.topicService.findById(id);

        return new ResponseEntity<>(topic, HttpStatus.OK);
    }

    @GetMapping("/search")
    ResponseEntity<List<TopicDTO>> findTopicsByName(@RequestParam("name") String name) {
        List<TopicDTO> topics = this.topicService.findTopicsByName(name);

        return new ResponseEntity<>(topics, HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<List<TopicDTO>> findAllTopics() {
        List<TopicDTO> topics = this.topicService.findAllTopics();

        return new ResponseEntity<>(topics, HttpStatus.OK);
    }

    @GetMapping("/{id}/articles")
    ResponseEntity<List<ArticleDTO>> findArticlesByTopicId(@PathVariable Long id) {
        List<ArticleDTO> articles = this.topicService.findArticlesByTopicId(id);

        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    /*
        About this endpoint, not sure if it's the correct path. In this request param we don't have required = false
        because if no name is provided an empty string will be assigned and an exception will be thrown.

        /topics?name=topic/articles is not valid, because query params are always in the end of the URI.
     */
    @GetMapping("/articles")
    ResponseEntity<List<ArticleDTO>> findArticlesByTopicName(@RequestParam(value = "topicName", defaultValue = "")
                                                             String topicName) {
        List<ArticleDTO> articles = this.topicService.findArticlesByTopicName(topicName);

        return new ResponseEntity<>(articles, HttpStatus.OK);
    }
}
