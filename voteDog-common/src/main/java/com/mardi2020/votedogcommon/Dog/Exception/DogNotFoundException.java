package com.mardi2020.votedogcommon.Dog.Exception;

public class DogNotFoundException extends RuntimeException {
    private static final String MESSAGE = "NOT_FOUND_DOG";

    public DogNotFoundException() {
        super(MESSAGE);
    }
}
