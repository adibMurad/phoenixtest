package com.example.phoenixtest.feign.exception;

import com.example.phoenixtest.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserDetailsFeignExceptionHandler {
    @ExceptionHandler
    @ResponseBody
    public ErrorResponse handleException(UserDetailsFeignException exception) {
        log.error(exception.getMessage(), exception);
        return ErrorResponse.of(exception.getCode(), exception.getName(), exception.getMessage());
    }
}
