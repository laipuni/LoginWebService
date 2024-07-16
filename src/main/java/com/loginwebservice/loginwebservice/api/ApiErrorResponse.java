package com.loginwebservice.loginwebservice.api;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiErrorResponse<T> {

    private HttpStatus status;
    private int code;
    private String message;
    private T data;

    private ApiErrorResponse(final HttpStatus status, final int code,final String message, final T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T>ApiErrorResponse<T> of(final HttpStatus status,final String message,final T data){
        return new ApiErrorResponse<>(status,status.value(),message,data);
    }

}
