package com.mardi2020.votedogvoteupdate.dog.service;

import com.mardi2020.votedogcommon.dog.enums.VoteStatus;
import com.mardi2020.votedogcommon.dog.exception.DogNotFoundException;
import com.mardi2020.votedogcommon.dog.message.DogParam;
import com.mardi2020.votedogcommon.dog.message.DogVoteUpdate;
import com.mardi2020.votedogcommon.dog.util.KafkaTopic;
import com.mardi2020.votedogvoteupdate.dog.Dog;
import com.mardi2020.votedogvoteupdate.dog.repository.DogRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DogsKafkaConsumer {

    private final DogRepository dogRepository;

    private final DogKafkaProducer dogKafkaProducer;

    @KafkaListener(topics = KafkaTopic.VOTE_BEFORE,
            containerFactory = "dogVoteUpdateConcurrentKafkaListenerContainerFactory")
    @Transactional
    public void modifyDog(final ConsumerRecord<String, DogVoteUpdate> record) {
        final DogVoteUpdate data = record.value();
        final WrapperDogs dogs = updateDogCount(data);
        DogParam dogParam = createMessageByStatus(data, dogs);
        dogKafkaProducer.send(KafkaTopic.VOTE_AFTER, dogParam);
    }

    public WrapperDogs updateDogCount(DogVoteUpdate updateData) {
        if (!updateData.getStatus().equals(VoteStatus.ANOTHER)) {
            return new WrapperDogs(handleStatus(updateData));
        }
        final Dog beforeDog = decreaseVoteCount(updateData.getBeforeDogId());
        final Dog newDog = increaseVoteCount(updateData.getNewDogId());
        return new WrapperDogs(newDog, beforeDog);
    }

    public DogParam createMessageByStatus(final DogVoteUpdate updateData, final WrapperDogs dogs) {
        DogParam dogParam = new DogParam(updateData);
        dogParam.setNewDogCount(dogs.getNewDog().getCount());
        if (!updateData.getStatus().equals(VoteStatus.ANOTHER)) {
            return dogParam;
        }
        dogParam.setBeforeDogCount(dogs.getBeforeDog().getCount());
        return dogParam;
    }

    public Dog increaseVoteCount(Long id) {
        final Dog dog = dogRepository.findById(id)
                .orElseThrow(DogNotFoundException::new);
        dog.increaseCount();
        return dogRepository.save(dog);
    }

    public Dog decreaseVoteCount(Long id) {
        final Dog dog = dogRepository.findById(id)
                .orElseThrow(DogNotFoundException::new);
        dog.decreaseCount();
        return dogRepository.save(dog);
    }

    public Dog handleStatus(DogVoteUpdate updateData) {
        if (updateData.getStatus().equals(VoteStatus.INIT)) {
            return increaseVoteCount(updateData.getNewDogId());
        }
        return decreaseVoteCount(updateData.getNewDogId());
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class WrapperDogs {
        private Dog newDog;
        private Dog beforeDog;

        public WrapperDogs(Dog newDog) {
            this.newDog = newDog;
        }

        public WrapperDogs(Dog newDog, Dog beforeDog) {
            this.newDog = newDog;
            this.beforeDog = beforeDog;
        }
    }
}
