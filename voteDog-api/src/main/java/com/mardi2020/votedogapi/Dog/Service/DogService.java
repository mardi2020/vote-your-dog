package com.mardi2020.votedogapi.Dog.Service;

import com.mardi2020.votedogapi.Dog.Dog;
import com.mardi2020.votedogapi.Dog.Dto.DogResponse.DogDetail;
import com.mardi2020.votedogapi.Dog.Dto.DogResponse.DogSimple;
import com.mardi2020.votedogapi.Dog.Repository.DogMongoDBRepository;
import com.mardi2020.votedogapi.Util.CookieUtil;
import com.mardi2020.votedogcommon.Dog.Enum.VoteStatus;
import com.mardi2020.votedogcommon.Dog.Exception.DogNotFoundException;
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
    public DogDetail findDogToDto(Long dogId) {
        final Dog dog = findDog(dogId);
        return new DogDetail(dog);
    }

    @Cacheable(value = "dog-list", key = "'all'")
    public List<DogSimple> findDogs(Pageable pageable) {
        final Page<Dog> dogs = dogRepository.findAll(pageable);
        return dogs.stream().map(DogSimple::new)
                .collect(Collectors.toList());
    }

    public CookieWithFlag voteProcess(Long dogId, String cookieDogId) {
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

    private Dog findDog(Long dogId) {
        return dogRepository.findByDogId(dogId)
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
