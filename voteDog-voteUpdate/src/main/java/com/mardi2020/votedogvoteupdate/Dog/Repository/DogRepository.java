package com.mardi2020.votedogvoteupdate.Dog.Repository;

import com.mardi2020.votedogvoteupdate.Dog.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogRepository extends JpaRepository<Dog, Long> {
}
