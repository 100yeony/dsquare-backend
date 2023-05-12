package com.ktds.dsquare.common.exception;

import javax.security.auth.login.LoginException;

public class LoginRequiredException extends LoginException {

    public LoginRequiredException() {
        super();
    }

    public LoginRequiredException(String msg) {
        super(msg);
    }

}
