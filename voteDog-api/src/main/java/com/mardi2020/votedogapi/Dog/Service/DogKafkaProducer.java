package com.mardi2020.votedogapi.Dog.Service;

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

    private final KafkaTemplate<String, DogVoteUpdate> kafkaTemplate;

    public void sendMessage(final Long newDogId,
                            final String beforeDogId,
                            final VoteStatus status) {
        DogVoteUpdate dogVoteUpdate = DogVoteUpdate.builder()
                .newDogId(newDogId)
                .beforeDogId(beforeDogId == null ? null : Long.valueOf(beforeDogId))
                .status(status)
                .build();

        kafkaTemplate.send(KafkaTopic.VOTE_BEFORE, dogVoteUpdate);
        log.info("Kafka produces message: " + dogVoteUpdate);
    }
}
