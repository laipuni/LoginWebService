package com.loginwebservice.loginwebservice.api;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse<Object> bindException(BindException exception){
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
        return ApiErrorResponse.of(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                null
        );
    }


}
