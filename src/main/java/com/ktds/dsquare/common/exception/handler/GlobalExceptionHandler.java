package com.ktds.dsquare.common.exception.handler;

import com.ktds.dsquare.common.enums.ResponseType;
import com.ktds.dsquare.common.exception.MemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({ MemberException.class })
    private ResponseEntity<ResponseType> badLoginExceptionHandler(MemberException e) {
        log.info(String.valueOf(e));
        return new ResponseEntity<>(ResponseType._401_FAILED_LOGIN, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<?> globalRuntimeExceptionHandler(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
