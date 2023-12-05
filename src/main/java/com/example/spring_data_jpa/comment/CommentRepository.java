package com.example.spring_data_jpa.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


interface CommentRepository extends JpaRepository<Comment, Long> {
    /*
        Fetches a comment with a given article id and comment id.

        It's very important to query for both the article id and the comment id. Querying for just the comment when the
        user made a request in the articles/3/comments/5 would result in returning the comment even if the article with
        id 3 does not exist, the comment was just on another article. Or if articles/invalidInput/comments/5 also would
        return the comment if we don't query for both

        Best practise for when we want to return a weak entity like comment that can exist without article is to query
        for both since comment has the article id in the ManyToOne relationship
     */
    @Query("""
                SELECT c
                FROM Comment c
                WHERE c.article.id = :articleId AND c.id = :commentId
            """)
    Optional<Comment> findCommentByArticleIdAndCommentId(@Param("articleId") Long articleId,
                                                         @Param("commentId")Long commentId);

    /*
        Fetches all comments for a given article on descending order based on their created date.
     */
    @Query("""
                SELECT c
                FROM Comment c
                JOIN FETCH c.article a
                WHERE c.article.id = :articleId
                ORDER BY c.createdDate DESC
            """)
    List<Comment> findCommentsByArticleIdOrderByCreatedDateDesc(@Param("articleId") Long articleId);


}
