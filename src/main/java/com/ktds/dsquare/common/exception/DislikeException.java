package com.ktds.dsquare.common.exception;

public class DislikeException extends RuntimeException {

    public DislikeException() {
        super("Dislike Exception");
    }

    public DislikeException(String message) {
        super(message);
    }

}
