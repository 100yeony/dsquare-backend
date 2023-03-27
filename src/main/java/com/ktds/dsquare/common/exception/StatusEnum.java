package com.ktds.dsquare.common.exception;

import lombok.Getter;

@Getter
public enum StatusEnum {


    TEST_FAIL("0000","testFail"),
    TEST_SUCCESS("1000","testSuccess"),
    UNKNOWN_ERROR("1001", "Unknown error")

    ;

    String code;
    String message;

    StatusEnum(String code, String message){
        this.code = code;
        this.message = message;
    }
}
