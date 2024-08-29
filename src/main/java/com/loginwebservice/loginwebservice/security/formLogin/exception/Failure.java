package com.loginwebservice.loginwebservice.security.formLogin.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Failure {

    LOGIN_ID_NOT_FOUND(
            new UsernameNotFoundException(
                    "존재하지 않는 계정입니다. 회원가입을 해주세요"
            )
    ),
    PASSWORD_NOT_FOUND(
            new BadCredentialsException(
                    "아이디 혹은 비밀번호가 맞지 않습니다."
            )
    ),
    SYSTEM_ERROR(
            new InternalAuthenticationServiceException(
                    "내부 시스템 문제로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요."
            )
    ),
    AUTHENTICATION_REJECTED(
            new AuthenticationCredentialsNotFoundException(
                    "인증 요청이 거부 되었습니다. 관리자에게 문의해주십시오."
            )
    ),
    LOGIN_FAIL(
            new LoginLockException(
                    "해당 계정은 5번이상 로그인 시도 했습니다. 15분간 계정이 보안 상태로 들어갑니다."
            )
    ),
    NOT_AUTHENTICATION_USER(
            new NotAuthenticationUserException(
                    "해당 계정은 인증하지 않았습니다. 회원가입 인증 이메일을 확인해주세요"
            )
    );

    private final Exception exceptionMatch;

    public static String findErrorMessage(Class<?> exception){
        return Arrays.stream(Failure.values())
                .filter(failure -> failure.getExceptionMatch().getClass().isAssignableFrom(exception))
                .findFirst()
                .map(failure -> failure.getExceptionMatch().getMessage())
                .orElse("알 수 없는 오류로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요.");
    }
}
