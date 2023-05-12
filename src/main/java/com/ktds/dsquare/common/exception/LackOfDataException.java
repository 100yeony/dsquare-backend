package com.ktds.dsquare.common.exception;

public class LackOfDataException extends RuntimeException {

    public LackOfDataException() {
        super("There is no content");
    }

    public LackOfDataException(String message) {
        super(message);
    }

}
