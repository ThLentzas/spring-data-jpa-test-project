package com.example.spring_data_jpa.comment;

public record CommentCreateRequest(
        String content,
        String username
) {
}
