package com.loginwebservice.loginwebservice.domain.user;

import com.loginwebservice.loginwebservice.domain.BaseEntity;
import com.loginwebservice.loginwebservice.domain.content.Content;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String userName;

    @Column(length = 12,unique = true)
    private String loginId;

    @Column()
    private String password;

    @Column(nullable = false,unique = true)
    private String email;

    private String picture;

    @Getter(value = AccessLevel.NONE)
    @OneToMany(mappedBy = "user")
    private List<Content> contents = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private boolean isAuthenticated;

    @Builder
    private User(
                 final String name,
                 final String email,
                 final String picture,
                 final Role role,
                 final String userName,
                 final String loginId,
                 final String password
            ) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.userName = userName;
        this.loginId = loginId;
        this.password = password;
        this.isAuthenticated = false;
    }

    public static User of(
            final String name,
            final String email,
            final String picture,
            final Role role,
            final String userName,
            final String loginId,
            final String password
    ){
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(role)
                .userName(userName)
                .loginId(loginId)
                .password(password)
                .build();
    }

    public String getRoleKey(){
        return role.getKey();
    }

    public User update(final String userName,final String name, final String email, final String picture) {
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.picture = picture;
        return this;
    }

    public void addContent(final Content content){
        this.contents.add(content);
    }

    public void resetPassword(final String password) {
        if(password == null){
            throw new IllegalArgumentException("해당 비밀번호로 변경할 수 없습니다.");
        }
        this.password = password;
    }

    public void changeAuthentication(){
        this.isAuthenticated = true;
    }

}
