package com.example.spring_data_jpa.topic;

import com.example.spring_data_jpa.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class TopicService {
    private final TopicRepository topicRepository;
    private static final TopicDTOMapper mapper = new TopicDTOMapper();

    TopicDTO createTopic(TopicCreateRequest createRequest) {
        if(this.topicRepository.existsByNameIgnoringCase(createRequest.name())) {
            throw new DuplicateResourceException("Topic with name already exists: " + createRequest.name());
        }

        Topic topic = new Topic(createRequest.name());
        this.topicRepository.save(topic);

        return mapper.apply(topic);
    }
}
