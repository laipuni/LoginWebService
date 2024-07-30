package com.loginwebservice.loginwebservice.domain.content.response;

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
    private String userName;
    private String picture;

    @Builder
    private ContentAddResponse(final Long contentId, final LocalDate createDate, final String contents,final String picture, final String userName) {
        this.contentId = contentId;
        this.createDate = createDate;
        this.contents = contents;
        this.picture = picture;
        this.userName = userName;
    }

    public static ContentAddResponse of(final Content content) {
        return ContentAddResponse.builder()
                .contentId(content.getId())
                .createDate(content.getCreateDate())
                .contents(content.getContents())
                .userName(content.getUserName())
                .picture(content.getPicture())
                .build();
    }
}
