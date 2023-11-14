package com.example.spring_data_jpa.article;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


interface ArticleRepository extends JpaRepository<Article, Long> {
    boolean existsByTitle(String title);
    boolean existsById(@NonNull Long id);
    /*
        The below queries are not necessary, because Spring Data will create the derived queries from the name of the
        method. I wrote them to get used to JPQL.
     */
    @Query("""
                SELECT a FROM Article a
                WHERE a.title ILIKE (CONCAT('%', :title, '%'))
            """)
    Optional<List<Article>> findArticlesByTitleContainingIgnoringCase(@Param("title") String title);
    @Query("""
                SELECT a FROM Article a
                WHERE a.content ILIKE (CONCAT('%', :content, '%'))
            """)
    Optional<List<Article>> findArticlesByContentContainingIgnoringCase(@Param("content") String content);
}
