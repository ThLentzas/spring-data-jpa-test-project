package com.example.spring_data_jpa.topic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import com.example.spring_data_jpa.AbstractUnitTest;


class TopicRepositoryTest extends AbstractUnitTest {
    @Autowired
    private TopicRepository topicRepository;

    @BeforeEach
    void setup() {
        Topic cloud = new Topic("Cloud");
        cloud.setStatus(TopicStatus.APPROVED);
        this.topicRepository.save(cloud);

        Topic science = new Topic("Science");
        science.setStatus(TopicStatus.APPROVED);
        this.topicRepository.save(science);

        this.topicRepository.save(new Topic("Education"));
        this.topicRepository.save(new Topic("Cloud Engineering"));
        this.topicRepository.save(new Topic("Chemistry"));
    }

    /*
        findByNameIgnoreCase() is also tested here
     */
    @BeforeEach
    void setupDates() {
        this.topicRepository.findByNameIgnoreCase("Cloud").ifPresent(
                topic -> topic.setCreatedDate(LocalDate.parse("2023-03-01"))
        );
    }

    @Test
    void shouldReturnTrueWhenSearchingForATopicThatExistsByName() {
        assertThat(this.topicRepository.existsByNameIgnoreCase("Cloud")).isTrue();
    }

    @Test
    void shouldFindTopicsOrderedByStatusAndCreatedDateDesc() {
        List<Topic> actual = this.topicRepository.findTopicsOrderByStatusAndCreatedDateDesc();

        assertThat(actual)
                .hasSize(5)
                .isSortedAccordingTo(Comparator
                        .comparing(Topic::getStatus, Comparator.reverseOrder())
                        .thenComparing(Topic::getCreatedDate, Comparator.reverseOrder()));

        /*
            Checks that the list contains exactly the given elements in the given order. ContainsExactly  asserts both
            the order and the values.
        */
        assertThat(actual)
                .extracting(Topic::getName, Topic::getStatus)
                .containsExactly(
                        tuple("Science", TopicStatus.APPROVED),
                        tuple("Cloud", TopicStatus.APPROVED),
                        tuple("Education", TopicStatus.CREATED),
                        tuple("Cloud Engineering", TopicStatus.CREATED),
                        tuple("Chemistry", TopicStatus.CREATED)
                );
    }

    @Test
    void shouldFindTopicsByNameContainingIgnoringCaseOrderedByCreatedDateDesc() {
        List<Topic> actual = this.topicRepository.findTopicsByNameContainingIgnoreCaseOrderByCreatedDateDesc("Cloud");

        assertThat(actual)
                .hasSize(2)
                .isSortedAccordingTo(Comparator
                        .comparing(Topic::getCreatedDate, Comparator.reverseOrder()));

        assertThat(actual)
                .extracting(Topic::getName)
                .containsExactly("Cloud Engineering", "Cloud");
    }
}
