package com.mardi2020.votedogapi.dog.controller.handler;

import com.mardi2020.votedogapi.error.errorcode.CommonErrorCode;
import com.mardi2020.votedogapi.error.errorcode.DogErrorCode;
import com.mardi2020.votedogapi.error.response.ErrorResponse;
import com.mardi2020.votedogcommon.dog.exception.DogNotFoundException;
import com.mardi2020.votedogcommon.dog.exception.ErrorCode;
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> serverError(final Exception e) {
        log.warn("handling exception: ", e);
        final CommonErrorCode internalServerError = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(internalServerError);
    }

    private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(new ErrorResponse(
                        errorCode.name(),
                        errorCode.getMessage()
                ));
    }
}