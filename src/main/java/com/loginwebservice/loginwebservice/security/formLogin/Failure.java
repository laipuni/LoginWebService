package com.loginwebservice.loginwebservice.security.formLogin;

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
    );

    private final Exception errorMatch;

    public static String findErrorMessage(Class<?> exception){
        return Arrays.stream(Failure.values())
                .filter(failure -> failure.getErrorMatch().getClass().isAssignableFrom(exception))
                .findFirst()
                .map(failure -> failure.getErrorMatch().getMessage())
                .orElse("알 수 없는 오류로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요.");
    }
}