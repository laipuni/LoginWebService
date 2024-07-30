package com.loginwebservice.loginwebservice.domain.content.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ContentAddServiceRequest {

    private String content;
    private String email;

    @Builder
    private ContentAddServiceRequest(final String content, final String email) {
        this.content = content;
        this.email = email;
    }

    public static ContentAddServiceRequest of(final String content, final String email){
        return ContentAddServiceRequest.builder()
                .content(content)
                .email(email)
                .build();
    }
}
