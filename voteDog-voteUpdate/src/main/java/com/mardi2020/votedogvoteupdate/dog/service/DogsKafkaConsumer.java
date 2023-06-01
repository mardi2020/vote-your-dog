package com.mardi2020.votedogvoteupdate.dog.service;

import com.mardi2020.votedogcommon.dog.enums.VoteStatus;
import com.mardi2020.votedogcommon.dog.exception.DogNotFoundException;
import com.mardi2020.votedogcommon.dog.message.DogParam;
import com.mardi2020.votedogcommon.dog.message.DogVoteUpdate;
import com.mardi2020.votedogcommon.dog.util.KafkaTopic;
import com.mardi2020.votedogvoteupdate.dog.Dog;
import com.mardi2020.votedogvoteupdate.dog.repository.DogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DogsKafkaConsumer {

    private final DogRepository dogRepository;

    private final DogKafkaProducer dogKafkaProducer;

    @KafkaListener(topics = KafkaTopic.VOTE_BEFORE,
            containerFactory = "dogVoteUpdateConcurrentKafkaListenerContainerFactory")
    public void modifyDog(final ConsumerRecord<String, DogVoteUpdate> record) {
        final DogVoteUpdate data = record.value();
        final VoteStatus status = data.getStatus();
        DogParam dogParam = new DogParam(status, data.getNewDogId(), data.getBeforeDogId());

        if (!status.equals(VoteStatus.ANOTHER)) {
            final Dog newDog = handleStatus(data.getNewDogId(), status);
            dogParam.setNewDogCount(newDog.getCount());
        } else {
            final Dog beforeDog = decreaseVoteCount(data.getBeforeDogId());
            final Dog newDog = increaseVoteCount(data.getNewDogId());
            dogParam.setBeforeDogCount(beforeDog.getCount());
            dogParam.setNewDogCount(newDog.getCount());
        }
        dogKafkaProducer.send(KafkaTopic.VOTE_AFTER, dogParam);
    }

    private Dog increaseVoteCount(Long id) {
        final Dog dog = dogRepository.findById(id)
                .orElseThrow(DogNotFoundException::new);
        dog.increaseCount();
        return dogRepository.save(dog);
    }

    private Dog decreaseVoteCount(Long id) {
        final Dog dog = dogRepository.findById(id)
                .orElseThrow(DogNotFoundException::new);
        dog.decreaseCount();
        return dogRepository.save(dog);
    }

    private Dog handleStatus(Long id, VoteStatus status) {
        if (status.equals(VoteStatus.INIT)) {
            return increaseVoteCount(id);
        }
        return decreaseVoteCount(id);
    }
}