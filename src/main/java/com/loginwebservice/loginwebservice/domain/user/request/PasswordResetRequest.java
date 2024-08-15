package com.loginwebservice.loginwebservice.domain.user.request;

import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordResetRequest {
    private String token;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*])[A-Za-z\\d~!@#$%^&*]{8,15}$",
            message = "비밀번호 : 길이는 최소 8문자 최대 15문자, 특수문자 1개, 대문자 1개, 소문자 1개, 숫자 1개이상 사용해 주세요"
    )
    private String password;
}
