package com.example.spring_data_jpa.comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import com.example.spring_data_jpa.article.Article;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;

/*
    In Hibernate 6.3 we can use @JdbcType(PostgreSQLEnumJdbcType.class) instead. The current version of Hibernate for
    Spring Data JPA is 6.2.13.
 */
@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @CreatedDate
    private LocalDate createdDate;
    @Column(nullable = false)
    private String content;
    @Column(columnDefinition = "comment_topic_status", nullable = false)
    @Type(PostgreSQLEnumType.class)
    private CommentStatus status;
    private String username;
    @ManyToOne
    private Article article;

    public Comment() {
        this.status = CommentStatus.CREATED;
    }

    Comment(String content, String username) {
        this.content = content;
        this.status = CommentStatus.CREATED;
        this.username = username;
    }
}
