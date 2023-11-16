package com.example.spring_data_jpa.article;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface ArticleRepository extends JpaRepository<Article, Long> {
    boolean existsByTitle(String title);

    boolean existsById(@NonNull Long id);

    /*
        The below queries are not necessary, because Spring Data will create the derived queries from the name of the
        method. I wrote them to get used to JPQL.
     */
    @Query("""
                SELECT a
                FROM Article a
                WHERE a.title
                ILIKE (CONCAT('%', :title, '%'))
            """)
    List<Article> findByTitleContainingIgnoringCase(@Param("title") String title);

    @Query("""
                SELECT a
                FROM Article a
                WHERE a.content
                ILIKE (CONCAT('%', :content, '%'))
            """)
    List<Article> findByContentContainingIgnoringCase(@Param("content") String content);

    /*
        Retrieving all published Articles order by their published date.

        Removing the PSQL query will still give us the same result, because the method name follows the pattern that
        Spring Data needs to derive the query.

        If we wanted to retrieve all articles by their status findAllByOrderByStatusDesc() and the correct will be
        generated.
     */
    @Query("""
                SELECT a
                FROM Article a
                WHERE a.status = :status
                ORDER BY a.publishedDate DESC
            """)
    List<Article> findAllByStatusOrderByPublishedDateDesc(@Param("status") ArticleStatus status);

    /*
        Retrieving all articles that are not published order by their status and created date in descending order.

        This query cant be generated with a method name from Spring Data.
        findAllByStatusNotOrderByCreatedDateDesc(ArticleStatus status) would work, but we need a second criteria to
        order so a derived query would not work.
     */
    @Query("""
                SELECT a
                FROM Article a
                WHERE NOT a.status = :status
                ORDER BY a.status DESC, a.createdDate DESC
            """)
    List<Article> findAllNonPublishedOrderByStatusAndCreatedDateDesc(@Param("status") ArticleStatus status);

    /*
        Retrieving all non-published articles with the given status order by their created date.

        Removing the PSQL query will still give us the same result, because the method name follows the pattern that
        Spring Data needs to derive the query.
     */
    @Query("""
                SELECT a
                FROM Article a
                WHERE a.status = :status
                ORDER BY a.createdDate DESC
            """)
    List<Article> findAllByStatusOrderByCreatedDateDesc(@Param("status") ArticleStatus status);

    /*
        Retrieving all published articles that their published date is in the given date range order by their
        published date.

        Removing the PSQL query will still give us the same result, because the method name follows the pattern that
        Spring Data needs to derive the query.
     */
    @Query("""
                SELECT a
                FROM Article a
                WHERE a.status = :status AND a.publishedDate BETWEEN :startDate AND :endDate
                ORDER BY a.publishedDate DESC
            """)
    List<Article> findAllByStatusAndPublishedDateBetween(@Param("status") ArticleStatus status,
                                                         @Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);

    /*
        Retrieving all non-published articles that their created date is in the given date range order by their
        created date.

        Removing the PSQL query will still give us the same result, because the method name follows the pattern that
        Spring Data needs to derive the query.
     */
    @Query("""
                SELECT a
                FROM Article a
                WHERE NOT a.status = :status AND a.createdDate BETWEEN :startDate AND :endDate
                ORDER BY a.createdDate DESC
            """)
    List<Article> findAllByStatusNotAndCreatedDateBetween(ArticleStatus status,
                                                          @Param("startDate") LocalDate startDate,
                                                          @Param("endDate") LocalDate endDate);

    /*
        Native SQL query for join table named article_topic.

        SELECT a.id, a.content
        FROM article a
        INNER JOIN article_topic at ON a.id = at.article_id
        WHERE at.topic_id = ?

        The JPQL query is not necessary, Spring Data will derive the correct query by the method name
     */
    @Query("""
                SELECT a
                FROM Article a
                JOIN a.topics t
                WHERE t.id = :topicId
           """)
    List<Article> findAllByTopicsId(@Param("topicId") Long topicId);
}
