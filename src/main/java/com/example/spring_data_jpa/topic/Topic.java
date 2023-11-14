package com.example.spring_data_jpa.topic;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDate;


/*
    In Hibernate 6.3 we can use @JdbcType(PostgreSQLEnumJdbcType.class) instead. The current version of Hibernate for
    Spring Data JPA is 6.2.13.
 */
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}, name = "unique_topic_name")
})
@Getter
@Setter
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate createdDate;
    @Column(columnDefinition = "status", nullable = false)
    @Type(PostgreSQLEnumType.class)
    private TopicStatus status;

    public Topic() {
        this.createdDate = LocalDate.now();
        this.status = TopicStatus.CREATED;
    }

    public Topic(String name) {
        this.name = name;
        this.createdDate = LocalDate.now();
        this.status = TopicStatus.CREATED;
    }
}
