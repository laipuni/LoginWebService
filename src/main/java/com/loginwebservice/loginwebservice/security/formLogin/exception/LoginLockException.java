package com.loginwebservice.loginwebservice.security.formLogin.exception;


import org.springframework.security.core.AuthenticationException;

public class LoginLockException extends AuthenticationException {

    public LoginLockException(final String message) {
        super(message);
    }

    public LoginLockException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
