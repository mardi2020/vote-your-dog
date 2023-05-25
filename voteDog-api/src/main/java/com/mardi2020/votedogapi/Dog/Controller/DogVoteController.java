package com.mardi2020.votedogapi.Dog.Controller;

import com.mardi2020.votedogapi.Dog.Service.DogKafkaProducer;
import com.mardi2020.votedogapi.Dog.Service.DogService;
import com.mardi2020.votedogapi.Dog.Service.DogService.CookieWithFlag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vote")
public class DogVoteController {

    private final DogKafkaProducer dogKafkaProducer;

    private final DogService dogService;

    @PostMapping("/{dogId}")
    public ResponseEntity<?> vote(@PathVariable Long dogId,
                                  @CookieValue(name="vote-cookie", required = false) String voteInfo) {
        final CookieWithFlag cookieWithFlag = dogService.voteProcess(dogId, voteInfo);
        dogKafkaProducer.sendMessage(dogId, voteInfo, cookieWithFlag.getStatus());

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, String.valueOf(cookieWithFlag.getCookie()))
                .body(cookieWithFlag.getStatus() + ": " + dogId + " vote success");
    }
}
