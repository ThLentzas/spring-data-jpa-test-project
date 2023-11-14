package com.example.spring_data_jpa.article;

import java.time.LocalDate;
import java.util.Set;

import com.example.spring_data_jpa.topic.TopicDTO;

record ArticleDTO(
        Long id,
        String title,
        String content,
        ArticleStatus status,
        LocalDate createdDate,
        LocalDate publishedDate,
        Set<TopicDTO> topics) {
}
