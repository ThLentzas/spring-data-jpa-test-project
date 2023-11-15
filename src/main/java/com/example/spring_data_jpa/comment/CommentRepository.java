package com.example.spring_data_jpa.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

interface CommentRepository extends JpaRepository<Comment, Long> {
    /*
        The below queries are not necessary, because Spring Data will create the derived queries from the name of the
        method. I wrote them to get used to JPQL.

        It's very important to query for both the article id and the comment id. Querying for just the comment when the
        user made a request in the articles/3/comments/5 would result in returning the comment even if the article with
        id 3 does not exist, the comment was just on another article.
     */
    @Query("""
                SELECT c
                FROM Comment c
                WHERE c.article.id = :articleId AND c.id = :commentId
            """)
    Optional<Comment> findByArticleIdAndId(Long articleId, Long commentId);

}