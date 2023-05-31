package com.mardi2020.votedogapi.Dog.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mardi2020.votedogapi.Dog.Dog;
import com.mardi2020.votedogapi.Dog.Repository.DogMongoDBRepository;
import com.mardi2020.votedogcommon.Dog.Enum.VoteStatus;
import com.mardi2020.votedogcommon.Dog.Exception.DogNotFoundException;
import com.mardi2020.votedogcommon.Dog.Message.DogParam;
import com.mardi2020.votedogcommon.Dog.Message.DogParam.DogInfo;
import com.mardi2020.votedogcommon.Dog.Util.KafkaTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DogKafkaConsumer {

    private final ObjectMapper mapper;

    private final DogMongoDBRepository dogRepository;

    @CacheEvict(value = "dog-list", key = "'all'")
    @KafkaListener(topics = KafkaTopic.VOTE_AFTER)
    public void handleCount(final String message) throws JsonProcessingException {
        log.info(message);
        final DogParam dogParam = mapper.readValue(message, DogParam.class);
        final VoteStatus status = dogParam.getVoteStatus();

        updateDogCount(dogParam.getNewDog());
        if (status.equals(VoteStatus.ANOTHER)) {
            updateDogCount(dogParam.getBeforeDog());
        }
    }

    @CachePut(value = "dog-update", key = "#dogInfo.dogId")
    public Dog updateDogCount(final DogInfo dogInfo) {
        final Dog dog = dogRepository.findByDogId(dogInfo.getDogId())
                .orElseThrow(DogNotFoundException::new);
        dog.updateCount(dogInfo.getCount());
        return dogRepository.save(dog);
    }
}
