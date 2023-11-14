package com.example.spring_data_jpa.topic;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;


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
}
