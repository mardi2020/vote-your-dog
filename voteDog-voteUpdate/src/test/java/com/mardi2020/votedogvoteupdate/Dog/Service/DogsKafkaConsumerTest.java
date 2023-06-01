package com.mardi2020.votedogvoteupdate.Dog.Service;

import static org.assertj.core.api.Assertions.assertThat;
import com.mardi2020.votedogcommon.Dog.Enum.VoteStatus;
import com.mardi2020.votedogcommon.Dog.Message.DogVoteUpdate;
import com.mardi2020.votedogcommon.Dog.Util.KafkaTopic;
import com.mardi2020.votedogvoteupdate.Dog.Dog;
import com.mardi2020.votedogvoteupdate.Dog.Repository.DogRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DogsKafkaConsumerTest {

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private DogsKafkaConsumer dogsKafkaConsumer;

    @BeforeEach
    void setUp() {
        dogRepository.save(Dog.builder()
                .id(1L)
                .count(10)
                .description("강아지 소개글")
                .name("코코")
                .image("http://naver.com/image")
                .build());
        dogRepository.save(Dog.builder()
                .id(2L)
                .count(20)
                .description("강아지 소개글2")
                .name("초코")
                .image("http://naver.com/image")
                .build());
    }

    @Test
    @DisplayName("처음 투표할 경우(INIT)")
    void first_vote() {
        // given
        final ConsumerRecord<String, DogVoteUpdate> record = new ConsumerRecord<>(
                KafkaTopic.VOTE_BEFORE, 0, 0, null,
                new DogVoteUpdate(1L, null, VoteStatus.INIT)
        );
        final Long dogId = 1L;
        final int count = dogRepository.findById(dogId).get().getCount();

        // when
        dogsKafkaConsumer.modifyDog(record);

        // then
        final Dog dog = dogRepository.findById(dogId).get();
        assertThat(dog.getCount()).isEqualTo(count + 1);
    }

    @Test
    @DisplayName("같은 강아지 후보를 취소할 경우(CANCEL)")
    void cancel_vote() {
        // given
        final ConsumerRecord<String, DogVoteUpdate> record = new ConsumerRecord<>(
                KafkaTopic.VOTE_BEFORE, 0, 0, null,
                new DogVoteUpdate(1L, 1L, VoteStatus.CANCEL)
        );
        final Long dogId = 1L;
        final int count = dogRepository.findById(dogId).get().getCount();

        // when
        dogsKafkaConsumer.modifyDog(record);

        // then
        final Dog dog = dogRepository.findById(dogId).get();
        assertThat(dog.getCount()).isEqualTo(count - 1);
    }

    @Test
    @DisplayName("다른 강아지를 눌러 기존 강아지 후보를 취소한 경우(ANOTHER)")
    void another_vote() {
        // given
        final Long beforeDogId = 1L;
        final Long afterDogId = 2L;
        final ConsumerRecord<String, DogVoteUpdate> record = new ConsumerRecord<>(
                KafkaTopic.VOTE_BEFORE, 0, 0, null,
                new DogVoteUpdate(afterDogId, beforeDogId, VoteStatus.ANOTHER)
        );
        final int beforeDogCount = dogRepository.findById(beforeDogId).get().getCount();
        final int afterDogCount = dogRepository.findById(afterDogId).get().getCount();

        // when
        dogsKafkaConsumer.modifyDog(record);

        // then
        final Dog beforeDog = dogRepository.findById(beforeDogId).get();
        final Dog afterDog = dogRepository.findById(afterDogId).get();
        assertThat(beforeDog.getCount()).isEqualTo(beforeDogCount - 1);
        assertThat(afterDog.getCount()).isEqualTo(afterDogCount + 1);
    }
}