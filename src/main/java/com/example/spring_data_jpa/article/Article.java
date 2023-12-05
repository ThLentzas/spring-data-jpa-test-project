package com.example.spring_data_jpa.article;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.Set;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;

import com.example.spring_data_jpa.topic.Topic;

/*
    In Hibernate 6.3 we can use @JdbcType(PostgreSQLEnumJdbcType.class) instead. The current version of Hibernate for
    Spring Data JPA is 6.2.13.
 */
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"title"}, name = "unique_article_title")
})
@EqualsAndHashCode
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(columnDefinition = "article_status", nullable = false)
    @Type(PostgreSQLEnumType.class)
    private ArticleStatus status;
    @Column(nullable = false)
    @CreatedDate
    private LocalDate createdDate;
    @Temporal(TemporalType.DATE)
    private LocalDate publishedDate;
    @ManyToMany
    @JoinTable(
            name = "article_topic",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    private Set<Topic> topics;

    public Article() {
        this.status = ArticleStatus.CREATED;
    }

    public Article(String title, String content, Set<Topic> topics) {
        this.title = title;
        this.content = content;
        this.status = ArticleStatus.CREATED;
        this.topics = topics;
    }
}
