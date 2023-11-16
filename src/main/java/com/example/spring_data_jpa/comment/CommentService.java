package com.example.spring_data_jpa.comment;

import com.example.spring_data_jpa.article.Article;
import com.example.spring_data_jpa.article.ArticleRepository;
import com.example.spring_data_jpa.article.ArticleStatus;
import com.example.spring_data_jpa.exception.ResourceNotFoundException;

import com.example.spring_data_jpa.exception.StatusConflictException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private static final CommentDTOMapper commentDTOMapper = new CommentDTOMapper();
    private static final String ARTICLE_NOT_FOUND_ERROR_MSG = "Article was not found with id: ";

    public CommentDTO addComment(Long articleId, CommentCreateRequest commentCreateRequest) {
        Article article = this.articleRepository.findById(articleId).orElseThrow(() ->
                new ResourceNotFoundException(ARTICLE_NOT_FOUND_ERROR_MSG + articleId));

        if(!article.getStatus().equals(ArticleStatus.PUBLISHED)) {
            throw new ResourceNotFoundException(ARTICLE_NOT_FOUND_ERROR_MSG + articleId);
        }

        Comment comment = new Comment(commentCreateRequest.content(), commentCreateRequest.username());
        comment.setArticle(article);
        this.commentRepository.save(comment);

        return commentDTOMapper.apply(comment);
    }

    public void updateCommentStatus(Long articleId, Long commentId, String action) {
        /*
            The relationship between comment and article is unidirectional from the comment side, so its ManyToOne and
            when we fetch the comment an extra query will be executed to fetch article, because by default its EAGER.
            So in total 3 queries for this method:  1 to fetch the comment, 1 to fetch the article of the comment, 1 to
            update the comment.
        */
        Comment comment = this.commentRepository.findByArticleIdAndId(articleId, commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment was not found with id: " + commentId));

        if(comment.getStatus().equals(CommentStatus.APPROVED)) {
            throw new IllegalArgumentException("Comment has been approved");
        }

        if(!action.isBlank() && action.equalsIgnoreCase("approve")) {
            comment.setStatus(CommentStatus.APPROVED);
            this.commentRepository.save(comment);
        }

        if(!action.isBlank() && action.equalsIgnoreCase("reject")) {
            this.commentRepository.delete(comment);
        }

        //Here some other value than approve or reject was provided, so we should handle with some exception
    }

    public void updateComment(Long articleId, Long commentId, CommentUpdateRequest updateRequest) {
        /*
            The relationship between comment and article is unidirectional from the comment side, so its ManyToOne and
            when we fetch the comment an extra query will be executed to fetch article, because by default its EAGER.
            So in total 3 queries for this method:  1 to fetch the comment, 1 to fetch the article of the comment, 1 to
            update the comment.
         */
        Comment comment = this.commentRepository.findByArticleIdAndId(articleId, commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment was not found with id: " + commentId));

        if(comment.getStatus().equals(CommentStatus.APPROVED)) {
            throw new StatusConflictException("Comment is already approved and cannot be modified");
        }

        if(updateRequest.content() == null || updateRequest.content().isBlank()) {
            throw new IllegalArgumentException("No content was provided");
        }

        comment.setContent(updateRequest.content());
        this.commentRepository.save(comment);
    }

    public List<CommentDTO> finAllCommentsByArticleId(Long articleId) {
        List<Comment> comments = this.commentRepository.findAllByArticleIdOrderByCreatedDateDesc(articleId);

        return comments.stream()
                .map(commentDTOMapper)
                .toList();

    }
}
