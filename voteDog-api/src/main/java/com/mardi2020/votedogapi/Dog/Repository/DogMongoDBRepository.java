package com.mardi2020.votedogapi.Dog.Repository;

import com.mardi2020.votedogapi.Dog.Dogs;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DogMongoDBRepository extends MongoRepository<Dogs, String> {

    Page<Dogs> findAll(Pageable pageable);

    Optional<Dogs> findByDogId(Long dogId);
}
