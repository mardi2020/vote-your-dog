package com.mardi2020.votedogapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class VoteDogApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoteDogApiApplication.class, args);
    }

}
