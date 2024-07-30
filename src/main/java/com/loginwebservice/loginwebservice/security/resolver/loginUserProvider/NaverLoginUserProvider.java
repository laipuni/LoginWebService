package com.loginwebservice.loginwebservice.security.resolver.loginUserProvider;

import com.loginwebservice.loginwebservice.security.resolver.dto.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Map;

//naver Login 방식으로 로그인하는 유저의 정보를 리졸브하는 provider
public class NaverLoginUserProvider implements AuthenticationLoginUserProvider{

    @Override
    public boolean supports(final Authentication authentication) {
        if(!OAuth2AuthenticationToken.class.isAssignableFrom(authentication.getClass())){
            return false;
        }

        OAuth2AuthenticationToken oauth2Authentication = (OAuth2AuthenticationToken) authentication;

        if(!"naver".equals(oauth2Authentication.getAuthorizedClientRegistrationId())){
            return false;
        }

        return true;
    }

    @Override
    public LoginUser resolve(final Authentication authentication) {
        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = user.getAttributes();
        return LoginUser.of(
                (String) attributes.get("name"),
                (String) attributes.get("email"),
                (String) attributes.get("profile_image")
        );
    }

}
