package com.mardi2020.votedogapi.Handler;

import com.mardi2020.votedogapi.Error.ErrorCode.CommonErrorCode;
import com.mardi2020.votedogapi.Error.ErrorCode.DogErrorCode;
import com.mardi2020.votedogapi.Error.Response.ErrorResponse;
import com.mardi2020.votedogcommon.Dog.Exception.DogNotFoundException;
import com.mardi2020.votedogcommon.Dog.Exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DogNotFoundException.class)
    ResponseEntity<Object> dogNotFound(final DogNotFoundException e) {
        log.warn("handling exception: ", e);
        final ErrorCode errorCode = DogErrorCode.DOG_NOT_FOUND;
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> illegalArgument(final IllegalArgumentException e) {
        log.warn("handling exception: ", e);
        final ErrorCode errorCode = CommonErrorCode.INVALID_ARGUMENTS;
        return handleExceptionInternal(errorCode);
    }

    private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(new ErrorResponse(
                        errorCode.name(),
                        errorCode.getMessage()
                ));
    }
}