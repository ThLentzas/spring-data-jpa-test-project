package com.example.spring_data_jpa.topic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import com.example.spring_data_jpa.AbstractUnitTest;


class TopicRepositoryTest extends AbstractUnitTest {
    @Autowired
    private TopicRepository topicRepository;

    @Test
    @Sql("/scripts/INSERT_TOPICS.sql")
    void shouldFindAllTopicsOrderByStatusAndCreatedDateDesc() {
        List<Topic> topics = this.topicRepository.findAllOrderByStatusAndCreatedDateDesc();

        assertThat(topics)
                .hasSize(5)
                .isSortedAccordingTo(Comparator
                        .comparing(Topic::getStatus, Comparator.reverseOrder())
                        .thenComparing(Topic::getCreatedDate, Comparator.reverseOrder()));

        /*
            Checks that the list contains exactly the given elements in the given order. ContainsExactly  asserts both
            the order and the values.
        */
        assertThat(topics)
                .extracting(Topic::getName, Topic::getStatus)
                .containsExactly(
                        tuple("Health", TopicStatus.APPROVED),
                        tuple("Technology", TopicStatus.APPROVED),
                        tuple("Science", TopicStatus.CREATED),
                        tuple("Cooking", TopicStatus.CREATED),
                        tuple("Education", TopicStatus.CREATED)
                );

    }
}
