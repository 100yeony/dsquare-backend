package com.ktds.dsquare.common.exception;

public class BadLoginException extends MemberException {

    public BadLoginException() {
        super("Login Failed.");
    }

    public BadLoginException(String message) {
        super(message);
    }

}
