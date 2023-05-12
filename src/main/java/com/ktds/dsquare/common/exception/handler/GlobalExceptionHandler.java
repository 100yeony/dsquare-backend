package com.ktds.dsquare.common.exception.handler;

import com.ktds.dsquare.common.enums.ResponseType;
import com.ktds.dsquare.common.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<?> globalRuntimeExceptionHandler(RuntimeException e) {
        log.error("Caught RE", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ IllegalStateException.class })
    private ResponseEntity<ResponseType> illegalStateExceptionHandler(IllegalStateException e) {
        log.info(String.valueOf(e));
        return new ResponseEntity<>(ResponseType._400_BAD_REQUEST, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(LackOfDataException.class)
    private ResponseEntity<?> LackOfDataExceptionHandler(LackOfDataException e) {
        log.info(String.valueOf(e));
        return new ResponseEntity<>(ResponseType._400_LACK_OF_DATA, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BoardTypeException.class)
    private ResponseEntity<?> BoardTypeExceptionHandler(BoardTypeException e) {
        log.info(String.valueOf(e));
        return new ResponseEntity<>(ResponseType._400_INVAlID_BOARD_TYPE, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DeleteQuestionException.class)
    private ResponseEntity<?> DeleteQuestionExceptionHandler(DeleteQuestionException e) {
        log.info(String.valueOf(e));
        return new ResponseEntity<>(ResponseType._400_DELETE_QUESTION_FAILED, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ MemberException.class })
    private ResponseEntity<ResponseType> badLoginExceptionHandler(MemberException e) {
        log.info(String.valueOf(e));
        return new ResponseEntity<>(ResponseType._401_FAILED_LOGIN, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<?> EntityNotFoundExceptionHandler(EntityNotFoundException e) {
        log.info(String.valueOf(e));
        return new ResponseEntity<>(ResponseType._404_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(PostNotFoundException.class)
    private ResponseEntity<?> PostNotFoundExceptionHandler(PostNotFoundException e) {
        log.info(String.valueOf(e));
        return new ResponseEntity<>(ResponseType._404_POST_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<?> LackOfDataExceptionHandler(UserNotFoundException e) {
        log.info(String.valueOf(e));
        return new ResponseEntity<>(ResponseType._404_USER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LikeException.class)
    private ResponseEntity<?> LikeExceptionHandler(LikeException e) {
        log.info(String.valueOf(e));
        return new ResponseEntity<>(ResponseType._409_LIKE_STATUS_CONFLICT, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(DislikeException.class)
    private ResponseEntity<?> DisikeExceptionHandler(DislikeException e) {
        log.info(String.valueOf(e));
        return new ResponseEntity<>(ResponseType._409_DISLIKE_STATUS_CONFLICT, HttpStatus.CONFLICT);
    }
}
