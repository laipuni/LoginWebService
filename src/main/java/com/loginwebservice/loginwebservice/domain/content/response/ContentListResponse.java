package com.loginwebservice.loginwebservice.domain.content.response;

import com.loginwebservice.loginwebservice.domain.content.Content;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentListResponse {

    private int size;
    private List<ContentViewResponse> contents;

    @Builder
    private ContentListResponse(final int size, final List<ContentViewResponse> contents) {
        this.size = size;
        this.contents = contents;
    }

    public static ContentListResponse of(final List<Content> contentList) {
        return ContentListResponse.builder()
                .size(contentList.size())
                .contents(
                        contentList.stream()
                        .map(ContentViewResponse::of)
                        .toList()
                )
                .build();
    }
}
