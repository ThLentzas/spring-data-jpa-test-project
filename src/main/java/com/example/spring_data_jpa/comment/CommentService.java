package com.example.spring_data_jpa.comment;

import com.example.spring_data_jpa.article.Article;
import com.example.spring_data_jpa.article.ArticleService;
import com.example.spring_data_jpa.article.ArticleStatus;
import com.example.spring_data_jpa.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleService articleService;
    private static final CommentDTOMapper commentDTOMapper = new CommentDTOMapper();

    public CommentDTO addComment(Long articleId, CommentCreateRequest commentCreateRequest) {
        Article article = this.articleService.findArticleById(articleId);
        if(!article.getStatus().equals(ArticleStatus.PUBLISHED)) {
            throw new ResourceNotFoundException("Article was not found with id: " + articleId);
        }

        Comment comment = new Comment(commentCreateRequest.content(), commentCreateRequest.username());
        comment.setArticle(article);
        this.commentRepository.save(comment);

        return commentDTOMapper.apply(comment);
    }
}
