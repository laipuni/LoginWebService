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
    private String userName;
    private String picture;

    @Builder
    private ContentViewResponse(final Long contentId, final String contents, final LocalDate createDate,final String userName, final String picture) {
        this.contentId = contentId;
        this.contents = contents;
        this.createDate = createDate;
        this.userName = userName;
        this.picture = picture;
    }

    public static ContentViewResponse of(Content content){
        return ContentViewResponse.builder()
                .contentId(content.getId())
                .contents(content.getContents())
                .createDate(content.getCreateDate())
                .userName(content.getUserName())
                .picture(content.getPicture())
                .build();
    }
}
