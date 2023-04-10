package com.ktds.dsquare.common.exception;

public class RefreshTokenMismatchException extends RuntimeException {

    public RefreshTokenMismatchException() {
        super("Refresh token information does not match.");
    }

    public RefreshTokenMismatchException(String message) {
        super(message);
    }

}
