package com.loginwebservice.loginwebservice.security.oauth2.dto;


import com.loginwebservice.loginwebservice.domain.user.Role;
import com.loginwebservice.loginwebservice.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String,Object> attributes;
    private String attributesNameKey;
    private String userName;
    private String name;
    private String email;
    private String picture;

    @Builder
    private OAuthAttributes(
            final Map<String, Object> attributes,
            final String attributesNameKey,
            final String name,
            final String userName,
            final String email,
            final String picture
    ) {
        this.attributes = attributes;
        this.attributesNameKey = attributesNameKey;
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(
            final String registrationId,
            final String attributeName,
            final Map<String, Object> attributes
    ) {
        if("naver".equals(registrationId)){
            return ofNaver("id",attributes);
        }
        return ofGoogle(attributeName,attributes);
    }

    private static OAuthAttributes ofGoogle(final String attributeName, final Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .attributes(attributes)
                .attributesNameKey(attributeName)
                .userName((String) attributes.get("name"))
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .build();
    }

    private static OAuthAttributes ofNaver(final String attributesName,final Map<String,Object> attributes){
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuthAttributes.builder()
                .attributes(response)
                .attributesNameKey(attributesName)
                .userName((String) response.get("nickname"))
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .build();
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .build();
    }
}
