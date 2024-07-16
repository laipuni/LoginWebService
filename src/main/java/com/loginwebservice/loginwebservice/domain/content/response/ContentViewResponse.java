package com.loginwebservice.loginwebservice.domain.content.response;

import com.loginwebservice.loginwebservice.domain.content.Content;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ContentViewResponse {

    private Long contentId;
    private String contents;
    private LocalDate createDate;

    @Builder
    private ContentViewResponse(final Long contentId, final String contents, final LocalDate createDate) {
        this.contentId = contentId;
        this.contents = contents;
        this.createDate = createDate;
    }

    public static ContentViewResponse of(Content content){
        return ContentViewResponse.builder()
                .contentId(content.getId())
                .contents(content.getContents())
                .createDate(content.getCreateDate())
                .build();
    }
}
