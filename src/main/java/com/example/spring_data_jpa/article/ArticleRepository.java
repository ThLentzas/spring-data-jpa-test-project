package com.example.spring_data_jpa.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import lombok.NonNull;


public interface ArticleRepository extends JpaRepository<Article, Long> {
    boolean existsByTitle(String title);

    boolean existsById(@NonNull Long id);

    /*
        Using findById for the GET request findArticleById() would result in a N+1 query problem. We have to create a
        method for this case with our own JPQL query to address the problem.

        We use findById() for the scenarios that would not result in N+1 query problem, like updating the status of an
        article.
     */
    @Query("""
                SELECT a
                FROM Article a
                JOIN FETCH a.topics t
                WHERE a.id = :id
            """)
    Optional<Article> findArticleByIdFetchingTopics(@Param("id") Long id);

    /*
        The below queries follow the correct method naming for Spring Data to create a derived query.
        When the articleDTOMapper calls the getTopics() because the fetch type is LAZY for every article a query will
        be executed to fetch all the relevant topics creating an N+1 issue.  By providing our own JPQL query we solve
        the N+1 select issue
     */
    @Query("""
                SELECT a
                FROM Article a
                JOIN FETCH a.topics t
                WHERE a.title
                ILIKE (CONCAT('%', :title, '%'))
            """)
    List<Article> findArticlesByTitleContainingIgnoringCase(@Param("title") String title);

    @Query("""
                SELECT a
                FROM Article a
                JOIN FETCH a.topics t
                WHERE a.content
                ILIKE (CONCAT('%', :content, '%'))
            """)
    List<Article> findArticlesByContentContainingIgnoringCase(@Param("content") String content);

    /*
        Retrieving all published Articles order by their published date.

        When the articleDTOMapper calls the getTopics() because the fetch type is LAZY for every article a query will
        be executed to fetch all the relevant topics creating an N+1 issue.  By providing our own JPQL query we solve
        the N+1 select issue
     */
    @Query("""
                SELECT a
                FROM Article a
                JOIN FETCH a.topics t
                WHERE a.status = :status
                ORDER BY a.publishedDate DESC
            """)
    List<Article> findPublishedArticlesOrderByPublishedDateDesc(@Param("status") ArticleStatus status);

    /*
        Retrieving all articles that are not published order by their status and created date in descending order.

        This query cant be generated with a method name from Spring Data.
        findAllByStatusNotOrderByCreatedDateDesc(ArticleStatus status) would work, but we need a second criteria to
        order so a derived query would not work.

        When the articleDTOMapper calls the getTopics() because the fetch type is LAZY for every article a query will
        be executed to fetch all the relevant topics creating an N+1 issue.  By providing our own JPQL query we solve
        the N+1 select issue
     */
    @Query("""
                SELECT a
                FROM Article a
                JOIN FETCH a.topics t
                WHERE NOT a.status = :status
                ORDER BY a.status DESC, a.createdDate DESC
            """)
    List<Article> findNonPublishedArticlesOrderByStatusAndCreatedDateDesc(@Param("status") ArticleStatus status);

    /*
        Retrieving all non-published articles with the given status order by their created date.

        When the articleDTOMapper calls the getTopics() because the fetch type is LAZY for every article a query will
        be executed to fetch all the relevant topics creating an N+1 issue.  By providing our own JPQL query we solve
        the N+1 select issue
     */
    @Query("""
                SELECT a
                FROM Article a
                JOIN FETCH a.topics t
                WHERE a.status = :status
                ORDER BY a.createdDate DESC
            """)
    List<Article> findArticlesByStatusOrderByCreatedDateDesc(@Param("status") ArticleStatus status);

    /*
        Retrieving all published articles that their published date is in the given date range order by their
        published date.

        When the articleDTOMapper calls the getTopics() because the fetch type is LAZY for every article a query will
        be executed to fetch all the relevant topics creating an N+1 issue.  By providing our own JPQL query we solve
        the N+1 select issue
     */
    @Query("""
                SELECT a
                FROM Article a
                JOIN FETCH a.topics t
                WHERE a.status = :status AND a.publishedDate BETWEEN :startDate AND :endDate
                ORDER BY a.publishedDate DESC
            """)
    List<Article> findPublishedArticlesWithPublishedDateBetweenOrderByPublishedDateDesc(
            @Param("status") ArticleStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /*
        Retrieving all non-published articles that their created date is in the given date range order by their
        created date.

        When the articleDTOMapper calls the getTopics() because the fetch type is LAZY for every article a query will
        be executed to fetch all the relevant topics creating an N+1 issue.  By providing our own JPQL query we solve
        the N+1 select issue
     */
    @Query("""
                SELECT a
                FROM Article a
                JOIN FETCH a.topics t
                WHERE NOT a.status = :status AND a.createdDate BETWEEN :startDate AND :endDate
                ORDER BY a.createdDate DESC
            """)
    List<Article> findNonPublishedArticlesWithCreatedDateBetweenOrderByCreatedDesc(
            @Param("status") ArticleStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /*
        Native SQL query for join table named article_topic.

        SELECT a.id, a.content
        FROM article a
        INNER JOIN article_topic at ON a.id = at.article_id
        WHERE at.topic_id = ?

       When the articleDTOMapper calls the getTopics() because the fetch type is LAZY for every article a query will
       be executed to fetch all the relevant topics creating an N+1 issue.  By providing our own JPQL query we solve
       the N+1 select issue
     */
    @Query("""
                SELECT a
                FROM Article a
                JOIN a.topics t
                WHERE t.id = :topicId
           """)
    List<Article> findArticlesByTopicId(@Param("topicId") Long topicId);
}
