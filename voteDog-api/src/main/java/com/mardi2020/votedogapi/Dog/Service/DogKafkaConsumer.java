package com.mardi2020.votedogapi.Dog.Service;

import com.mardi2020.votedogapi.Dog.Dog;
import com.mardi2020.votedogapi.Dog.Repository.DogMongoDBRepository;
import com.mardi2020.votedogcommon.Dog.Enum.VoteStatus;
import com.mardi2020.votedogcommon.Dog.Exception.DogNotFoundException;
import com.mardi2020.votedogcommon.Dog.Message.DogParam;
import com.mardi2020.votedogcommon.Dog.Message.DogParam.DogInfo;
import com.mardi2020.votedogcommon.Dog.Util.KafkaTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DogKafkaConsumer {

    private final DogMongoDBRepository dogRepository;

    @CacheEvict(value = "dog-list", key = "'all'")
    @KafkaListener(topics = KafkaTopic.VOTE_AFTER,
            containerFactory = "voteKafkaListenerContainerFactory")
    public void handleCount(final ConsumerRecord<String, DogParam> consumerRecord) {
        final DogParam dogParam = consumerRecord.value();
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
