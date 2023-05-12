package com.ktds.dsquare.common.exception.handler;

import com.ktds.dsquare.common.enums.ResponseType;
import com.ktds.dsquare.common.exception.AccountAuthenticationFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class AuthenticationExceptionHandler {

    @ExceptionHandler({ AccountAuthenticationFailedException.class })
    private ResponseEntity<ResponseType> accountAuthenticationFailedExceptionHandler(AccountAuthenticationFailedException e) {
        log.warn(String.valueOf(e));
        return new ResponseEntity<>(ResponseType._400_BAD_REQUEST, HttpStatus.OK);
    }

}
