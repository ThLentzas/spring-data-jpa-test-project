package com.example.spring_data_jpa.article;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


interface ArticleRepository extends JpaRepository<Article, Long> {
    boolean existsByTitle(String title);
    Optional<Article> findByTitle(String title);
}
