package com.example.spring_data_jpa.article;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import com.example.spring_data_jpa.AbstractUnitTest;


class ArticleRepositoryTest extends AbstractUnitTest {
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    @Sql("/scripts/INSERT_ARTICLES.sql")
    void shouldFindAllNonPublishedArticlesOrderByStatusAndCreatedDateDesc() {
        List<Article> articles = this.articleRepository.findAllNonPublishedOrderByStatusAndCreatedDateDesc(
                ArticleStatus.PUBLISHED);

        assertThat(articles)
                .hasSize(4)
                .isSortedAccordingTo(Comparator
                        .comparing(Article::getStatus, Comparator.reverseOrder())
                        .thenComparing(Article::getCreatedDate, Comparator.reverseOrder()));

        /*
            Checks that the list contains exactly the given elements in the given order. ContainsExactly  asserts both
            the order and the values.
         */
        assertThat(articles)
                .extracting(Article::getTitle, Article::getStatus)
                .containsExactly(
                        tuple("Space Exploration", ArticleStatus.APPROVED),
                        tuple("Cooking", ArticleStatus.APPROVED),
                        tuple("Education", ArticleStatus.SUBMITTED),
                        tuple("Healthy Living", ArticleStatus.SUBMITTED)
                );
    }
}
