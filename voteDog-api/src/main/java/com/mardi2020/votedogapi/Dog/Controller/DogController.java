package com.mardi2020.votedogapi.Dog.Controller;

import com.mardi2020.votedogapi.Dog.Dto.DogResponse.DogDetail;
import com.mardi2020.votedogapi.Dog.Dto.DogResponse.DogSimple;
import com.mardi2020.votedogapi.Dog.Service.DogService;
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
public class DogController {

    private final DogService dogService;

    @GetMapping("/{dogId}")
    public ResponseEntity<?> dogDetails(@PathVariable Long dogId) {
        final DogDetail dog = dogService.findDog(dogId);
        return ResponseEntity.ok(dog);
    }

    @GetMapping
    public ResponseEntity<?> dogList(@PageableDefault(size=8, sort="count", direction = Direction.DESC)
                                         Pageable pageable) {
        final List<DogSimple> dogs = dogService.findDogs(pageable);
        return ResponseEntity.ok().body(dogs);
    }
}
