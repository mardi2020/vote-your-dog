package com.mardi2020.votedogcommon.Dog.Message;

import com.mardi2020.votedogcommon.Dog.Enum.VoteStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class DogParam {

    private VoteStatus voteStatus;

    private DogInfo newDog;

    private DogInfo beforeDog;

    public DogParam(VoteStatus status, Long newDogId, Long beforeDogId) {
        this.voteStatus = status;
        this.newDog = new DogInfo(newDogId);
        this.beforeDog = new DogInfo(beforeDogId);
    }

    public void setBeforeDogCount(int count) {
        this.beforeDog.setCount(count);
    }

    public void setNewDogCount(int count) {
        this.newDog.setCount(count);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor @Builder
    public static class DogInfo {
        private Long dogId;
        private int count;

        public DogInfo(Long id) {
            this.dogId = id;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
