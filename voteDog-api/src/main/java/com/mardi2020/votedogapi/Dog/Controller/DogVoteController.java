package com.mardi2020.votedogapi.Dog.Controller;

import com.mardi2020.votedogapi.Dog.Service.DogKafkaProducer;
import com.mardi2020.votedogapi.Dog.Service.DogService;
import com.mardi2020.votedogapi.Dog.Service.DogService.CookieWithFlag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/votes")
@Tag(name = "dog votes", description = "강아지 투표 기능 API")
public class DogVoteController {

    private final DogKafkaProducer dogKafkaProducer;

    private final DogService dogService;

    @Operation(summary = "Update 강아지 투표 상태", description = "사용자는 하나의 강아지만 투표할 수 있다")
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
