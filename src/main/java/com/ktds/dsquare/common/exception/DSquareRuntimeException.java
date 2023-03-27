package com.ktds.dsquare.common.exception;

import lombok.Getter;

@Getter
public class DSquareRuntimeException extends RuntimeException{

    private String code = "9999";

    public DSquareRuntimeException(String errCode, String message) {
        super(message);
        this.code = errCode;
    }
}
