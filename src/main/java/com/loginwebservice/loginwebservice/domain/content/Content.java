package com.loginwebservice.loginwebservice.domain.content;

import com.loginwebservice.loginwebservice.domain.BaseEntity;
import com.loginwebservice.loginwebservice.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String userName;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    private User user;

    private String picture;

    @Builder
    private Content(final String contents, final String userName, final String picture,final User user) {
        this.contents = contents;
        this.userName = userName;
        this.picture = picture;
        this.user = user;
    }

    public static Content of(final User user,final String contents){
        Content content = Content.builder()
                .contents(contents)
                .picture(user.getPicture())
                .userName(user.getUserName())
                .user(user)
                .build();
        user.addContent(content);
        return content;
    }
}
