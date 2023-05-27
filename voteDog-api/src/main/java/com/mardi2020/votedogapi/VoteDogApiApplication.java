package com.mardi2020.votedogapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(servers = {
        @Server(url = "https://vote-your-dog.ngrok.app", description = "default URL")
})
public class VoteDogApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoteDogApiApplication.class, args);
    }

}
