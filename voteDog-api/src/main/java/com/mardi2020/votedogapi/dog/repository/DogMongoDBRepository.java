package com.mardi2020.votedogapi.dog.repository;

import com.mardi2020.votedogapi.dog.Dog;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DogMongoDBRepository extends MongoRepository<Dog, String> {

    Page<Dog> findAll(Pageable pageable);

    Optional<Dog> findByDogId(Long dogId);
}
