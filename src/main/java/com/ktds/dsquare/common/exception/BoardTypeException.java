package com.ktds.dsquare.common.exception;

public class BoardTypeException extends RuntimeException {

    public BoardTypeException() {
        super("Invalid Board Type.");
    }

    public BoardTypeException(String message) {
        super(message);
    }

}
