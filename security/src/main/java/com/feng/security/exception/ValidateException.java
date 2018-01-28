package com.feng.security.exception;


import org.springframework.security.core.AuthenticationException;

public class ValidateException extends AuthenticationException {

    public ValidateException(String msg, Throwable t) {
        super(msg, t);
    }

    public ValidateException(String msg) {
        super(msg);
    }
}
