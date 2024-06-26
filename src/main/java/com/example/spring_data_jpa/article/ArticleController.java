package com.example.spring_data_jpa.article;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.spring_data_jpa.comment.CommentDTO;
import com.example.spring_data_jpa.comment.CommentService;
import com.example.spring_data_jpa.comment.CommentCreateRequest;
import com.example.spring_data_jpa.comment.CommentUpdateRequest;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.web.util.UriComponentsBuilder;

/*
    This Controller contains both endpoints for Article and Comment, because Comment is a weak Entity and can only
    exist with an Article. In a different case we could have a CommentController where for example we could return all
    comments for a specific user.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;

    @PostMapping
    ResponseEntity<ArticleDTO> createArticle(@RequestBody ArticleCreateRequest createRequest,
                                             UriComponentsBuilder uriBuilder) {
        ArticleDTO articleDTO = this.articleService.createArticle(createRequest);
        URI location = uriBuilder
                .path("/api/v1/articles/{id}")
                .buildAndExpand(articleDTO.id())
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> updateArticle(@PathVariable Long id, @RequestBody ArticleUpdateRequest articleUpdateRequest) {
        this.articleService.updateArticle(id, articleUpdateRequest);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/status")
    ResponseEntity<Void> updateArticleStatus(@PathVariable Long id,
                                             @RequestBody ArticleUpdateStatusRequest articleUpdateStatusRequest) {
        this.articleService.updateArticleStatus(id, articleUpdateStatusRequest);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*
        In this endpoint since we can have a search with both the title and content or with either, we can't remove the
        required = false from either but if none is submitted an exception will be thrown because at least 1 query param
        is required.
     */
    @GetMapping("/search")
    ResponseEntity<List<ArticleDTO>> findArticlesByTitleAndOrContent(
            @RequestParam(value = "title", defaultValue = "", required = false) String title,
            @RequestParam(value = "content", defaultValue = "", required = false) String content) {
        List<ArticleDTO> articles = this.articleService.findArticlesByTitleAndOrContent(title, content);

        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<ArticleDTO> findArticleById(@PathVariable Long id) {
        ArticleDTO article = this.articleService.findArticleById(id);

        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<List<ArticleDTO>> findAllArticles(
            @RequestParam(value = "status", required = false) ArticleStatus status,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {
        List<ArticleDTO> articles = this.articleService.findAllArticles(status, startDate, endDate);

        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @PostMapping("/{articleId}/comments")
    ResponseEntity<CommentDTO> addComment(@PathVariable Long articleId,
                                          @RequestBody CommentCreateRequest commentCreateRequest,
                                          UriComponentsBuilder uriBuilder
                                          ) {
        CommentDTO commentDTO = this.commentService.addComment(articleId, commentCreateRequest);

        URI location = uriBuilder
                .path("/api/v1/articles/{articleId}/comments/{commentId}")
                .buildAndExpand(articleId, commentDTO.id())
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{articleId}/comments/{commentId}/status")
    ResponseEntity<Void> updateCommentStatus(@PathVariable Long articleId,
                                             @PathVariable Long commentId,
                                             @RequestParam(
                                                     value = "action",
                                                     defaultValue = "",
                                                     required = false) String action) {
        this.commentService.updateCommentStatus(articleId, commentId, action);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{articleId}/comments/{commentId}")
    ResponseEntity<Void> updateComment(@PathVariable Long articleId,
                                       @PathVariable Long commentId,
                                       @RequestBody CommentUpdateRequest commentUpdateRequest) {
        this.commentService.updateComment(articleId, commentId, commentUpdateRequest);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{articleId}/comments")
    ResponseEntity<List<CommentDTO>> findAllCommentsByArticleId(@PathVariable Long articleId) {
        List<CommentDTO> comments = this.commentService.finAllCommentsByArticleId(articleId);

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
