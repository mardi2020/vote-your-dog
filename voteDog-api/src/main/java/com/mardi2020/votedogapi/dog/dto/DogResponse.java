package com.mardi2020.votedogapi.dog.dto;


import com.mardi2020.votedogapi.dog.Dog;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DogResponse {

    public record DogDetail(Long dogId, String name, String description, String image, int count) {
        public static DogDetail of(Dog dog) {
            return new DogDetail(
                    dog.getDogId(),
                    dog.getName(),
                    dog.getDescription(),
                    dog.getImage(),
                    dog.getCount()
            );
        }
    }

    public record DogSimple(Long dogId, String name, String description, String image, int count) {
        public static DogSimple of(Dog dog) {
            return new DogSimple(
                    dog.getDogId(),
                    dog.getName(),
                    dog.getDescription(),
                    dog.getImage(),
                    dog.getCount()
            );
        }
    }
}
