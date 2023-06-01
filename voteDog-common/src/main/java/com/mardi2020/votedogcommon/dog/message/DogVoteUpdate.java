package com.mardi2020.votedogcommon.dog.message;

import com.mardi2020.votedogcommon.dog.enums.VoteStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @Builder
@NoArgsConstructor
@Data
public class DogVoteUpdate {

    private Long newDogId;

    private Long beforeDogId;

    private VoteStatus status;
}