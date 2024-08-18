package com.loginwebservice.loginwebservice.security.formLogin.exception;


import org.springframework.security.core.AuthenticationException;

public class LoginFailException extends AuthenticationException {

    public LoginFailException(final String message) {
        super(message);
    }

    public LoginFailException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
