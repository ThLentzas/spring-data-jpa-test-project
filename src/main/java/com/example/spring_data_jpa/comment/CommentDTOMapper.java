package com.example.spring_data_jpa.comment;

import java.util.function.Function;

public class CommentDTOMapper implements Function<Comment, CommentDTO> {

    @Override
    public CommentDTO apply(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getCreatedDate(),
                comment.getContent(),
                comment.getStatus(),
                comment.getUsername()
        );
    }
}
