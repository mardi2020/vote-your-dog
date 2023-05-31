package com.mardi2020.votedogapi.Dog.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mardi2020.votedogcommon.Dog.Enum.VoteStatus;
import com.mardi2020.votedogcommon.Dog.Message.DogVoteUpdate;
import com.mardi2020.votedogcommon.Dog.Util.KafkaTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DogKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper mapper;

    private void send(final String topic, final DogVoteUpdate data) {
        String message;
        try {
            message = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        kafkaTemplate.send(topic, message);
        log.info("Kafka produces message: " + message);
    }

    public void sendMessage(final Long newDogId,
                            final String beforeDogId,
                            final VoteStatus status) {
        DogVoteUpdate dogVoteUpdate = DogVoteUpdate.builder()
                .newDogId(newDogId)
                .beforeDogId(beforeDogId == null ? null : Long.valueOf(beforeDogId))
                .status(status)
                .build();

        send(KafkaTopic.VOTE_BEFORE, dogVoteUpdate);
    }
}
