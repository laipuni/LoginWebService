package com.loginwebservice.loginwebservice.domain.user.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class UserIdVerifyAuthCodeResponse {
    private String loginId;
}
