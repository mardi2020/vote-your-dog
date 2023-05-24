package com.mardi2020.votedogapi.Dog.Service;

import com.mardi2020.votedogapi.Dog.Dogs;
import com.mardi2020.votedogapi.Dog.Dto.DogResponse.DogDetail;
import com.mardi2020.votedogapi.Dog.Dto.DogResponse.DogSimple;
import com.mardi2020.votedogapi.Dog.Repository.DogMongoDBRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DogService {

    private final DogMongoDBRepository dogRepository;

    public DogDetail findDog(Long dogId) {
        final Dogs dog = dogRepository.findByDogId(dogId)
                .orElseThrow();
        return new DogDetail(dog);
    }

    public List<DogSimple> findDogs(Pageable pageable) {
        final Page<Dogs> dogs = dogRepository.findAll(pageable);
        return dogs.stream().map(DogSimple::new)
                .collect(Collectors.toList());
    }
}
