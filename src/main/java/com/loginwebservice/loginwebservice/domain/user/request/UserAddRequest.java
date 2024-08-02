package com.loginwebservice.loginwebservice.domain.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAddRequest {

    @Pattern(
            regexp = "^[a-z]{1}[a-zA-Z0-9_]{4,11}$",
            message = "아이디 : 5~12자의 영문 소문자, 숫자와 특수기호(_)만 사용 가능합니다."
    )
    private String loginId;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
            message = "비밀번호 : 길이는 최소 8문자 최대 15문자, 특수문자 1개, 대문자 1개, 소문자 1개, 숫자 1개이상 사용해 주세요"
    )
    private String password;

    @Pattern(
            regexp = "\\w+@\\w+\\.\\w+(\\.\\w+)?",
            message = "이메일 : 이메일 주소가 정확한지 확인해 주세요."
    )
    private String email;

    @NotBlank(message = "이름 : 필수 정보입니다.")
    private String name;

    @NotBlank(message = "닉네임 : 필수 정보입니다.")
    private String userName;

    @Builder
    private UserAddRequest(final String loginId, final String password, final String email, final String userName, final String name) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.userName = userName;
        this.name = name;
    }
}
