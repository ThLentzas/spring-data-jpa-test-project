package com.example.spring_data_jpa.comment;

import com.example.spring_data_jpa.article.Article;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDate;

/*
    In Hibernate 6.3 we can use @JdbcType(PostgreSQLEnumJdbcType.class) instead. The current version of Hibernate for
    Spring Data JPA is 6.2.13.
 */
@Entity
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate createdDate;
    @Column(nullable = false)
    private String content;
    @Column(columnDefinition = "status", nullable = false)
    @Type(PostgreSQLEnumType.class)
    private CommentStatus status;
    private String username;
    @ManyToOne
    private Article article;
}
