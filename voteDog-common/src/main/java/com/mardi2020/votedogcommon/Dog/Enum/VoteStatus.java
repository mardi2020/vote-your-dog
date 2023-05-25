package com.mardi2020.votedogcommon.Dog.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VoteStatus {
    INIT("first vote"), CANCEL("vote cancel"), ANOTHER("vote another");

    public final String value;
}
