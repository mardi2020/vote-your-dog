package com.mardi2020.votedogvoteupdate.Dog.Service;

import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mardi2020.votedogvoteupdate.Dog.Dog;
import com.mardi2020.votedogvoteupdate.Dog.Repository.DogRepository;
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
    void first_vote() throws JsonProcessingException {
        // given
        final String message = "{\"newDogId\":1,\"beforeDogId\":null,\"status\":\"INIT\"}";
        final Long dogId = 1L;
        final int count = dogRepository.findById(dogId).get().getCount();

        // when
        dogsKafkaConsumer.modifyDog(message);

        // then
        final Dog dog = dogRepository.findById(dogId).get();
        assertThat(dog.getCount()).isEqualTo(count + 1);
    }

    @Test
    @DisplayName("같은 강아지 후보를 취소할 경우(CANCEL)")
    void cancel_vote() throws JsonProcessingException {
        // given
        final String message = "{\"newDogId\":1,\"beforeDogId\":1,\"status\":\"CANCEL\"}";
        final Long dogId = 1L;
        final int count = dogRepository.findById(dogId).get().getCount();

        // when
        dogsKafkaConsumer.modifyDog(message);

        // then
        final Dog dog = dogRepository.findById(dogId).get();
        assertThat(dog.getCount()).isEqualTo(count - 1);
    }

    @Test
    @DisplayName("다른 강아지를 눌러 기존 강아지 후보를 취소한 경우(ANOTHER)")
    void another_vote() throws JsonProcessingException {
        // given
        final String message = "{\"newDogId\":2,\"beforeDogId\":1,\"status\":\"ANOTHER\"}";
        final Long beforeDogId = 1L;
        final Long afterDogId = 2L;
        final int beforeDogCount = dogRepository.findById(beforeDogId).get().getCount();
        final int afterDogCount = dogRepository.findById(afterDogId).get().getCount();

        // when
        dogsKafkaConsumer.modifyDog(message);

        // then
        final Dog beforeDog = dogRepository.findById(beforeDogId).get();
        final Dog afterDog = dogRepository.findById(afterDogId).get();
        assertThat(beforeDog.getCount()).isEqualTo(beforeDogCount - 1);
        assertThat(afterDog.getCount()).isEqualTo(afterDogCount + 1);
    }
}