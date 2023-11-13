package com.example.spring_data_jpa.article;

import com.example.spring_data_jpa.topic.Topic;

import java.util.Set;

record ArticleCreateRequest(
        String title,
        String content,
        Set<Topic> topics) {
}
