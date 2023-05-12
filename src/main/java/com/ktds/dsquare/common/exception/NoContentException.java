package com.ktds.dsquare.common.exception;

public class NoContentException extends RuntimeException {

    public NoContentException() {
        super("There is no content");
    }

    public NoContentException(String message) {
        super(message);
    }

}
