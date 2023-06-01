package com.mardi2020.votedogapi.dog.dto;


import com.mardi2020.votedogapi.dog.Dog;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DogResponse {

    @Data
    @NoArgsConstructor
    public static class DogDetail {
        private Long dogId;
        private String name;
        private String description;
        private String image;
        private int count;

        public DogDetail(Dog dog) {
            this.dogId = dog.getDogId();
            this.name = dog.getName();
            this.description = dog.getDescription();
            this.image = dog.getImage();
            this.count = dog.getCount();
        }
    }

    @Data
    @NoArgsConstructor
    public static class DogSimple {
        private Long dogId;
        private String name;
        private String description;
        private String image;
        private int count;

        public DogSimple(Dog dog) {
            this.dogId = dog.getDogId();
            this.name = dog.getName();
            this.description = dog.getDescription();
            this.image = dog.getImage();
            this.count = dog.getCount();
        }
    }
}
