package com.ktds.dsquare.common.exception;

public class AccessTokenStillValidException extends RuntimeException {

    public AccessTokenStillValidException() {
        super("Access token is not expired.");
    }

    public AccessTokenStillValidException(String message) {
        super(message);
    }

}
