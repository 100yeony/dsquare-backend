package com.ktds.dsquare.common.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class DSquareExceptionHandler {

//    @org.springframework.web.bind.annotation.ExceptionHandler({DSquareRuntimeException.class})
//    public ResponseEntity<StatusMsg> dsquareExceptionHandler(HttpServletRequest request, HttpServletResponse respone, final DSquareRuntimeException e) {
//        e.printStackTrace();
//        log.error("##### DSquareExceptionHandler.DSquareRuntimeException: {}", e);
//
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST) //400
//                .body(StatusMsg.builder()
//                        .errorCode(e.getCode())
//                        .errorMsg(e.getMessage())
//                        .build());
//    }
//
//    @org.springframework.web.bind.annotation.ExceptionHandler({Exception.class})
//    public ResponseEntity<StatusMsg> dsquareExceptionHandler(HttpServletRequest request, HttpServletResponse respone, final Exception e) {
//        e.printStackTrace();
//        log.error("##### DSquareExceptionHandler.ExceptionHandler: ", e);
//
//        return ResponseEntity
//                .status(500)
//                .body(StatusMsg.builder()
//                        .errorCode(StatusEnum.UNKNOWN_ERROR.getCode())
//                        .errorMsg(StatusEnum.UNKNOWN_ERROR.getMessage())
//                        .build());
//    }

}