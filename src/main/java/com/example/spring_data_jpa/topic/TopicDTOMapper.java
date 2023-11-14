package com.example.spring_data_jpa.topic;

import java.util.function.Function;

public class TopicDTOMapper implements Function<Topic, TopicDTO> {

    @Override
    public TopicDTO apply(Topic topic) {
        return new TopicDTO(
                topic.getId(),
                topic.getName(),
                topic.getStatus(),
                topic.getCreatedDate()
        );
    }
}
