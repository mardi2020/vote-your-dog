package com.mardi2020.votedogapi.dog.service;

import com.mardi2020.votedogapi.dog.Dog;
import com.mardi2020.votedogapi.dog.dto.DogResponse.DogDetail;
import com.mardi2020.votedogapi.dog.dto.DogResponse.DogSimple;
import com.mardi2020.votedogapi.dog.repository.DogMongoDBRepository;
import com.mardi2020.votedogapi.util.CookieUtil;
import com.mardi2020.votedogcommon.dog.enums.VoteStatus;
import com.mardi2020.votedogcommon.dog.exception.DogNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DogService {

    private final DogMongoDBRepository dogRepository;

    @Cacheable(value = "dog-detail", key = "#dogId", unless = "#result == null")
    public DogDetail findDogToDto(final Long dogId) {
        final Dog dog = findDog(dogId);
        return new DogDetail(dog);
    }

    @Cacheable(value = "dog-list", key = "'all'")
    public List<DogSimple> findDogs(final Pageable pageable) {
        final Page<Dog> dogs = dogRepository.findAll(pageable);
        return dogs.stream().map(DogSimple::new)
                .collect(Collectors.toList());
    }

    public CookieWithFlag voteProcess(final Long dogId, final String cookieDogId) {
        findDog(dogId);

        if (cookieDogId == null) {
            return new CookieWithFlag(VoteStatus.INIT,
                    CookieUtil.createCookie(dogId.toString()));
        }
        if (dogId.equals(Long.valueOf(cookieDogId))) {
            return new CookieWithFlag(VoteStatus.CANCEL, CookieUtil.removeCookie());
        }
        return new CookieWithFlag(VoteStatus.ANOTHER,
                CookieUtil.createCookie(dogId.toString()));
    }

    private Dog findDog(final Long id) {
        return dogRepository.findByDogId(id)
                .orElseThrow(
                        DogNotFoundException::new
                );
    }

    public record CookieWithFlag(VoteStatus status, ResponseCookie cookie) {
        public VoteStatus getStatus() {
            return this.status;
        }

        public ResponseCookie getCookie() {
            return this.cookie;
        }
    }
}
