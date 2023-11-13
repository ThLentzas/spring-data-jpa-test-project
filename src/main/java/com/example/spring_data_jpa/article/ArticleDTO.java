package com.example.spring_data_jpa.article;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.example.spring_data_jpa.comment.Comment;
import com.example.spring_data_jpa.topic.Topic;

record ArticleDTO(
        Long id,
        String title,
        String content,
        ArticleStatus status,
        LocalDate createdDate,
        LocalDate publishedDate,
        Set<Topic> topics,
        List<Comment> comments) {
}
