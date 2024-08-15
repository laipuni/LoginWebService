package com.loginwebservice.loginwebservice.domain.user.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordAuthCodeValidResponse {
    private String loginId;
}
