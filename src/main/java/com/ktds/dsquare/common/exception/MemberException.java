package com.ktds.dsquare.common.exception;

public class MemberException extends RuntimeException {

    public MemberException() {
        super("Wrong Member.");
    }

    public MemberException(String message) {
        super(message);
    }

}
