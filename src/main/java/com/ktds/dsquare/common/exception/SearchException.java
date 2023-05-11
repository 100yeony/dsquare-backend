package com.ktds.dsquare.common.exception;

public class SearchException extends RuntimeException {

    public SearchException() {
        super("Search Exception");
    }

    public SearchException(String message) {
        super(message);
    }

}
