package com.ktds.dsquare.common.exception;

public class DeleteQuestionException extends RuntimeException {

    public DeleteQuestionException() {
        super("Delete Question Exception");
    }

    public DeleteQuestionException(String message) {
        super(message);
    }

}
