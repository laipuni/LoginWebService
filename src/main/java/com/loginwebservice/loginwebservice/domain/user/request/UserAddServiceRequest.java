package com.loginwebservice.loginwebservice.domain.user.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserAddServiceRequest {

    private String loginId;
    private String password;
    private String email;
    private String name;
    private String userName;

    @Builder
    private UserAddServiceRequest(final String loginId, final String password, final String email, final String userName, final String name) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.userName = userName;
        this.name = name;
    }

    public static UserAddServiceRequest of(final UserAddRequest request) {
        return UserAddServiceRequest.builder()
                .loginId(request.getLoginId())
                .password(request.getPassword())
                .email(request.getEmail())
                .name(request.getName())
                .userName(request.getUserName())
                .build();
    }
}
