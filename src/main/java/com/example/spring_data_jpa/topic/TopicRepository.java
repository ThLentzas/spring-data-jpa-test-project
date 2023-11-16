package com.example.spring_data_jpa.topic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface TopicRepository extends JpaRepository<Topic, Long> {
    boolean existsByNameIgnoringCase(String name);

    /*
        Should return only 1 result otherwise we would have 2 topics with the same name and that would violate the
        unique constraint
     */
    Optional<Topic> findByNameIgnoreCase(String name);

    /*
        Retrieving all topics that have or contain the provided name order by their  created date in descending order.
        Removing the @Query would still work because Spring Data would create a derived query from the method's name
     */
    @Query("""
                SELECT t
                FROM Topic t
                WHERE t.name
                ILIKE (CONCAT('%', :name, '%'))
                ORDER BY t.createdDate DESC
            """)
    List<Topic> findAllByNameContainingIgnoreCaseOrderByCreatedDateDesc(String name);

    /*
        Retrieves all topics order by their status and created date in descending order.

        This query cant be generated with a method name from Spring Data. The 2nd criteria in the Order By is what would
        not allow us to use a derived query.
     */
    @Query("""
                SELECT t
                FROM Topic t
                ORDER BY t.status DESC, t.createdDate DESC
            """)
    List<Topic> findAllOrderByStatusAndCreatedDateDesc();
}
