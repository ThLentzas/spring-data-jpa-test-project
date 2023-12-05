package com.example.spring_data_jpa.comment;

import com.example.spring_data_jpa.AbstractUnitTest;
import com.example.spring_data_jpa.article.Article;
import com.example.spring_data_jpa.article.ArticleRepository;
import com.example.spring_data_jpa.article.ArticleStatus;
import com.example.spring_data_jpa.topic.Topic;
import com.example.spring_data_jpa.topic.TopicRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


class CommentRepositoryTest extends AbstractUnitTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    void setup() {
        Topic topic = this.topicRepository.save(new Topic("Cloud"));
        Article article = new Article(
                "Cloud Engineering",
                "Content about cloud engineering",
                Set.of(topic));
        article.setStatus(ArticleStatus.PUBLISHED);
        this.articleRepository.save(article);

        Comment comment = new Comment("content", "username");
        comment.setArticle(article);
        this.commentRepository.save(comment);
    }

    @Test
    void shouldFindCommentByArticleIdAndCommentId() {
        Comment comment = new Comment("new content", "new username");
        Article article = this.articleRepository.findArticlesByTitleContainingIgnoringCase("Cloud Engineering").get(0);
        comment.setArticle(article);
        comment = this.commentRepository.save(comment);

        this.commentRepository.findCommentByArticleIdAndCommentId(article.getId(), comment.getId()).ifPresent(
                actual -> {
                    assertThat(actual.getContent()).isEqualTo("new content");
                    assertThat(actual.getUsername()).isEqualTo("new username");
                }
        );
    }

    @Test
    void shouldFindCommentsByArticleIdOrderedByCreatedDateDesc() {
        Comment comment = new Comment("new content", "new username");
        Article article = this.articleRepository.findArticlesByTitleContainingIgnoringCase("Cloud Engineering").get(0);
        comment.setArticle(article);
        this.commentRepository.save(comment);

        List<Comment> actual = this.commentRepository.findCommentsByArticleIdOrderByCreatedDateDesc(article.getId());

        assertThat(actual)
                .hasSize(2)
                .isSortedAccordingTo(Comparator.comparing(Comment::getCreatedDate, Comparator.reverseOrder()));

        assertThat(actual)
                .extracting(Comment::getContent, Comment::getUsername)
                .containsExactly(
                        tuple("content", "username"),
                        tuple("new content", "new username")
                );
    }
}
