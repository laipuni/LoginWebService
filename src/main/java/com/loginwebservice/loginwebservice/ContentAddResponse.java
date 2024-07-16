package com.loginwebservice.loginwebservice;

import com.loginwebservice.loginwebservice.domain.content.Content;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentAddResponse {

    private Long contentId;
    private LocalDate createDate;
    private String contents;

    @Builder
    private ContentAddResponse(final Long contentId, final LocalDate createDate, final String contents) {
        this.contentId = contentId;
        this.createDate = createDate;
        this.contents = contents;
    }

    public static ContentAddResponse of(final Content content) {
        return ContentAddResponse.builder()
                .contentId(content.getId())
                .createDate(content.getCreateDate())
                .contents(content.getContents())
                .build();
    }
}
