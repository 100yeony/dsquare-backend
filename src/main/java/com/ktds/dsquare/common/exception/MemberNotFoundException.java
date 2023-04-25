package com.ktds.dsquare.common.exception;

public class MemberNotFoundException extends MemberException {

    public MemberNotFoundException() {
        super("Member not found");
    }

    public MemberNotFoundException(String username) {
        super("No such member with email/username " + username);
    }

}
