package com.example.spring_data_jpa.comment;

import java.time.LocalDate;

public record CommentDTO(
        Long id,
        LocalDate createdDate,
        String content,
        CommentStatus status,
        String username
) {
}
