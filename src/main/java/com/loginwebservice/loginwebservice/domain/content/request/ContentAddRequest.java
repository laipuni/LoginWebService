package com.loginwebservice.loginwebservice.domain.content.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentAddRequest {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Builder
    private ContentAddRequest(final String content) {
        this.content = content;
    }
}
