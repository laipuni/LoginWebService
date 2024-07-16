package com.loginwebservice.loginwebservice.domain.content;

import com.loginwebservice.loginwebservice.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content extends BaseEntity {

    @Id
    @GeneratedValue()
    private Long id;

    private String contents;

    @Builder
    private Content(final String contents) {
        this.contents = contents;
    }

    public static Content of(String contents){
        return Content.builder()
                .contents(contents)
                .build();
    }
}
