package com.example.spring_data_jpa.article;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.spring_data_jpa.comment.CommentCreateRequest;
import com.example.spring_data_jpa.comment.CommentDTO;
import com.example.spring_data_jpa.comment.CommentService;

import lombok.RequiredArgsConstructor;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;

    @PostMapping
    ResponseEntity<ArticleDTO> createArticle(@RequestBody ArticleCreateRequest createRequest) {
        ArticleDTO articleDTO = this.articleService.createArticle(createRequest);

        return new ResponseEntity<>(articleDTO, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/comments")
    ResponseEntity<CommentDTO> addComment(@PathVariable Long id,
                                          @RequestBody CommentCreateRequest commentCreateRequest) {
        CommentDTO commentDTO = this.commentService.addComment(id, commentCreateRequest);

        return new ResponseEntity<>(commentDTO, HttpStatus.CREATED);
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

    @GetMapping("/search")
    ResponseEntity<List<ArticleDTO>> findArticles(
            @RequestParam(value = "title", defaultValue = "", required = false) String title,
            @RequestParam(value = "content", defaultValue = "", required = false) String content) {

        List<ArticleDTO> articles = this.articleService.findArticles(title, content);

        return new ResponseEntity<>(articles, HttpStatus.OK);
    }


}
