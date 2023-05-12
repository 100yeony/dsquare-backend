package com.ktds.dsquare.common.exception;

public class LikeException extends RuntimeException {

    public LikeException() {
        super("Like Exception");
    }

    public LikeException(String message) {
        super(message);
    }

}
