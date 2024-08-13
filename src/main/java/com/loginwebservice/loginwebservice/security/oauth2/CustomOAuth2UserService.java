package com.loginwebservice.loginwebservice.security.oauth2;

import com.loginwebservice.loginwebservice.domain.user.User;
import com.loginwebservice.loginwebservice.domain.user.UserRepository;
import com.loginwebservice.loginwebservice.security.oauth2.dto.OAuthAttributes;
import com.loginwebservice.loginwebservice.security.oauth2.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService oauth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oauth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String attributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(
                registrationId,
                attributeName,
                oAuth2User.getAttributes()
        );

        User user = saveOrUpdate(attributes);

        httpSession.setAttribute("user",new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getAttributesNameKey()
        );
    }

    private User saveOrUpdate(final OAuthAttributes attributes) {
        User user = userRepository.findUserByEmail(attributes.getEmail())
                .map(findUser -> findUser.update(
                        attributes.getUserName(), attributes.getName(), attributes.getEmail(), attributes.getPicture()
                ))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
