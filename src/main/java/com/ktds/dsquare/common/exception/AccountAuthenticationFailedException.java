package com.ktds.dsquare.common.exception;

public class AccountAuthenticationFailedException extends RuntimeException {

    public AccountAuthenticationFailedException() {
        super("Failed to authenticate account.");
    }

    public AccountAuthenticationFailedException(String message) {
        super(message);
    }

}
