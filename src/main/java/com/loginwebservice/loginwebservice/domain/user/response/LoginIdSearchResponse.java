package com.loginwebservice.loginwebservice.domain.user.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginIdSearchResponse {
    private boolean isExist;
    private String token;

    public static LoginIdSearchResponse of(final boolean isExist, final String helpToken) {
        return LoginIdSearchResponse.builder()
                    .isExist(isExist)
                    .token(helpToken)
                    .build();
    }
}
