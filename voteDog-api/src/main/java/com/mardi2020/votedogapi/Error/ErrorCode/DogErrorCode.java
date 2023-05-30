package com.mardi2020.votedogapi.Error.ErrorCode;

import com.mardi2020.votedogcommon.Dog.Exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DogErrorCode implements ErrorCode {

    DOG_NOT_FOUND(HttpStatus.NOT_FOUND, "Dog not found");

    private final HttpStatus httpStatus;

    private final String message;
}
