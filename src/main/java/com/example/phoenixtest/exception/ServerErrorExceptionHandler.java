package com.example.phoenixtest.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerErrorException;

@Slf4j
@RestControllerAdvice
public class ServerErrorExceptionHandler {
    @ExceptionHandler
    @ResponseBody
    public ErrorResponse handleException(ServerErrorException exception) {
        log.error(exception.getMessage(), exception);
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, exception.getReason());
    }
}
