package com.ktds.dsquare.common.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException() {
        super("Post Not Found.");
    }

    public PostNotFoundException(String message) {
        super(message);
    }

}
