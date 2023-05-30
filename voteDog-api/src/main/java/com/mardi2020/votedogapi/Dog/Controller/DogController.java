package com.mardi2020.votedogapi.Dog.Controller;

import com.mardi2020.votedogapi.Dog.Dto.DogResponse.DogDetail;
import com.mardi2020.votedogapi.Dog.Dto.DogResponse.DogSimple;
import com.mardi2020.votedogapi.Dog.Service.DogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dogs")
@Tag(name = "dogs", description = "강아지 정보 조회 API")
public class DogController {

    private final DogService dogService;

    @Operation(summary = "GET 강아지", description = "특정 id의 강아지 정보 가져오기")
    @GetMapping("/{dogId}")
    public ResponseEntity<?> dogDetails(@PathVariable Long dogId) {
        final DogDetail dog = dogService.findDogToDto(dogId);
        return ResponseEntity.ok(dog);
    }

    @Operation(summary = "GET 강아지 리스트", description = "강아지 후보를 8개씩 가져오기")
    @GetMapping
    public ResponseEntity<?> dogList(@PageableDefault(size=8, sort="count", direction = Direction.DESC)
                                         Pageable pageable) {
        final List<DogSimple> dogs = dogService.findDogs(pageable);
        return ResponseEntity.ok().body(dogs);
    }
}
