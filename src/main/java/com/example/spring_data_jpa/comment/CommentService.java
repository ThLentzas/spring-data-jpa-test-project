package com.example.spring_data_jpa.comment;

import com.example.spring_data_jpa.article.Article;
import com.example.spring_data_jpa.article.ArticleRepository;
import com.example.spring_data_jpa.article.ArticleService;
import com.example.spring_data_jpa.article.ArticleStatus;
import com.example.spring_data_jpa.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private static final String ARTICLE_NOT_FOUND_ERROR_MSG = "Article was not found with id: ";
    private static final CommentDTOMapper commentDTOMapper = new CommentDTOMapper();

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
}
