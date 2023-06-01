package com.mardi2020.votedogcommon.dog.exception;

import lombok.Getter;

@Getter
public class DogNotFoundException extends RuntimeException {
    private ErrorCode errorCode;

    private static final String MESSAGE = "DOG_NOT_FOUND";

    public DogNotFoundException() {
        super(MESSAGE);
    }

    public DogNotFoundException(ErrorCode errorCode) {
        super(MESSAGE);
        this.errorCode = errorCode;
    }
}
