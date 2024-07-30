package com.loginwebservice.loginwebservice.security.resolver.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginUser {
    private String name;
    private String email;
    private String picture;

    @Builder
    private LoginUser(final String name, final String email, final String picture) {
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static LoginUser of(final String name, final String email, final String picture){
        return LoginUser.builder()
                .name(name)
                .picture(picture)
                .email(email)
                .build();
    }
}
