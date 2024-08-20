package com.loginwebservice.loginwebservice.security.formLogin.exception;

import org.springframework.security.core.AuthenticationException;

public class NotAuthenticationUserException extends AuthenticationException {
    public NotAuthenticationUserException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public NotAuthenticationUserException(final String msg) {
        super(msg);
    }
}
