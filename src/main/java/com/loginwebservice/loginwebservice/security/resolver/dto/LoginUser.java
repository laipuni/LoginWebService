package com.loginwebservice.loginwebservice.security.resolver.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginUser {
    private String userName;
    private String email;
    private String picture;

    @Builder
    private LoginUser(final String userName, final String email, final String picture) {
        this.userName = userName;
        this.email = email;
        this.picture = picture;
    }

    public static LoginUser of(final String userName, final String email, final String picture){
        return LoginUser.builder()
                .userName(userName)
                .picture(picture)
                .email(email)
                .build();
    }
}
