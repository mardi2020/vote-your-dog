package com.mardi2020.votedogapi.Dog.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mardi2020.votedogapi.Dog.Dog;
import com.mardi2020.votedogapi.Dog.Dto.DogResponse.DogDetail;
import com.mardi2020.votedogapi.Dog.Dto.DogResponse.DogSimple;
import com.mardi2020.votedogapi.Dog.Repository.DogMongoDBRepository;
import com.mardi2020.votedogcommon.Dog.Exception.DogNotFoundException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureDataMongo
@ActiveProfiles("test")
class DogServiceTest {

    @Autowired
    private DogService dogService;

    @Autowired
    private DogMongoDBRepository dogRepository;

    @Autowired
    CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        Dog dog = Dog.builder()
                .id("646c827195477070b44dbfa7")
                .dogId(1L)
                .count(50)
                .image("https://img2.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/202105/25/holapet/20210525081725668haup.png")
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
    @DisplayName("강아지의 id(dogId)로 세부적인 조회를 할 수 있다")
    void findDog() {
        // given
        final Long dogId = 1L;

        // when
        final DogDetail dog = dogService.findDogToDto(dogId);

        // then
        assertThat(dog.getDogId()).isEqualTo(dogId);
    }

    @Test
    @DisplayName("투표가능한 강아지 목록을 조회할 수 있다")
    void findDogs() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 5,
                Direction.DESC, "count");

        // when
        final List<DogSimple> dogs = dogService.findDogs(pageRequest);

        // then
        assertThat(dogs.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("강아지의 id가 없다면 예외가 발생한다")
    void findDogNotFound() {
        // given
        final Long dogId = 100L;

        // when, then
        assertThrows(DogNotFoundException.class,
                () -> dogService.findDogToDto(dogId));
    }

}