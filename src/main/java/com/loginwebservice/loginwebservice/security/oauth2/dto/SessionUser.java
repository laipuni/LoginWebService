package com.loginwebservice.loginwebservice.security.oauth2.dto;

import com.loginwebservice.loginwebservice.domain.user.User;
import lombok.Getter;

@Getter
public class SessionUser {

    private Long userSeq;
    private String name;
    private String email;
    private String picture;

    public SessionUser(final User user) {
        this.userSeq = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
