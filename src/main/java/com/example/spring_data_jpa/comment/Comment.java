package com.example.spring_data_jpa.comment;

import com.example.spring_data_jpa.article.Article;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import java.time.LocalDate;

/*
    In Hibernate 6.3 we can use @JdbcType(PostgreSQLEnumJdbcType.class) instead. The current version of Hibernate for
    Spring Data JPA is 6.2.13.
 */
@Entity
@Getter
@Setter
@ToString
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

    public Comment() {
        this.createdDate = LocalDate.now();
        this.status = CommentStatus.CREATED;
    }

    Comment(String content, String username) {
        this.createdDate = LocalDate.now();
        this.content = content;
        this.status = CommentStatus.CREATED;
        this.username = username;
    }
}
