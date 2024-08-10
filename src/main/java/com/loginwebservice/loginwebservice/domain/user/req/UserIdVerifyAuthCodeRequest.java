package com.loginwebservice.loginwebservice.domain.user.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserIdVerifyAuthCodeRequest {

    @NotBlank(message = "이름 : 해당 입력은 필수입니다.")
    private String name;

    @Pattern(
            regexp = "\\w+@\\w+\\.\\w+(\\.\\w+)?",
            message = "이메일 : 이메일 주소가 정확한지 확인해 주세요."
    )
    private String email;

    @NotBlank(message = "인증 코드 : 해당 입력은 필수입니다.")
    private String authCode;
}
