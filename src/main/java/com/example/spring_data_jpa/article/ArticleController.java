package com.example.spring_data_jpa.article;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
class ArticleController {
    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody ArticleCreateRequest createRequest) {
        ArticleDTO articleDTO = articleService.createArticle(createRequest);

        return new ResponseEntity<>(articleDTO, HttpStatus.CREATED);
    }
}
