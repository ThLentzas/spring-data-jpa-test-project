package com.example.spring_data_jpa.topic;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TopicRepository extends JpaRepository<Topic, Long> {
    boolean existsByNameIgnoringCase(String name);
    Optional<Topic> findByName(String name);
}
