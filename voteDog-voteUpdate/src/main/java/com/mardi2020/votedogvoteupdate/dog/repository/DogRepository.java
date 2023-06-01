package com.mardi2020.votedogvoteupdate.dog.repository;

import com.mardi2020.votedogvoteupdate.dog.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogRepository extends JpaRepository<Dog, Long> {
}
