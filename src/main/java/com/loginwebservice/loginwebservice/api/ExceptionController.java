package com.loginwebservice.loginwebservice.api;

import com.loginwebservice.loginwebservice.Email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {

    @Value("${spring.mail.username}")
    private String toDeveloper;

    private final EmailService emailService;

    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse<Object> bindException(BindException exception){
        log.trace("[bindException] msg = {}", exception.getMessage());
        return ApiErrorResponse.of(
                HttpStatus.BAD_REQUEST,
                exception.getBindingResult()
                        .getAllErrors().get(0)
                        .getDefaultMessage(),
                null
        );
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse<Object> IllegalArgumentException(IllegalArgumentException exception){
        log.trace("[IllegalArgumentException] msg = {}", exception.getMessage());
        return ApiErrorResponse.of(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                null
        );
    }

    @ExceptionHandler(value = IllegalStateException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse<Object> IllegalStateException(IllegalStateException exception){
        log.warn("[IllegalStateException] msg = {}, cause = {}", exception.getMessage(), exception.getCause());
        emailService.sendEmail(
                toDeveloper,
                exception.getCause().getLocalizedMessage(),
                exception.getMessage()
        );
        return ApiErrorResponse.of(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                null
        );
    }


}
