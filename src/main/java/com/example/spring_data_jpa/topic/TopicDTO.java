package com.example.spring_data_jpa.topic;

import java.time.LocalDate;

public record TopicDTO(
        Long id,
        String name,
        TopicStatus status,
        LocalDate createdDate
)  {
}
