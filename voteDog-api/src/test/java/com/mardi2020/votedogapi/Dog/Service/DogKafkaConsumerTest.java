package com.mardi2020.votedogapi.Dog.Service;

import static org.assertj.core.api.Assertions.assertThat;

import com.mardi2020.votedogapi.Dog.Dog;
import com.mardi2020.votedogapi.Dog.Repository.DogMongoDBRepository;
import com.mardi2020.votedogcommon.Dog.Message.DogParam.DogInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureDataMongo
@ActiveProfiles("test")
class DogKafkaConsumerTest {

    @Autowired
    private DogKafkaConsumer dogKafkaConsumer;

    @Autowired
    private DogMongoDBRepository dogRepository;

    @BeforeEach
    void setUp() {
        Dog dog = Dog.builder()
                .id("646c827195477070b44dbfa7")
                .dogId(1L)
                .count(50)
                .image("http://test.img")
                .description("test dog description")
                .name("koko")
                .build();

        dogRepository.save(dog);
    }

    @AfterEach
    void tearDown() {
        dogRepository.deleteAll();
    }

    @Test
    @DisplayName("갱신된 투표수를 반영한다(50 -> 100)")
    void updateDogCount() {
        // given
        final Long dogId = 1L;
        final DogInfo dogInfo = new DogInfo(dogId, 100);

        // when
        final Dog savedDog = dogKafkaConsumer.updateDogCount(dogInfo);

        // then
        assertThat(savedDog.getDogId()).isEqualTo(dogId);
    }
}