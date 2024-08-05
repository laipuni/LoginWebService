package com.loginwebservice.loginwebservice.security.resolver.loginUserProvider;

import com.loginwebservice.loginwebservice.security.formLogin.domain.SecurityUser;
import com.loginwebservice.loginwebservice.security.resolver.dto.LoginUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

//form Login 방식으로 로그인하는 유저의 정보를 리졸브하는 provider
public class FormLoginUserProvider implements AuthenticationLoginUserProvider{

    @Override
    public boolean supports(final Authentication authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication.getClass());
    }

    @Override
    public LoginUser resolve(final Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if(SecurityUser.class.isAssignableFrom(principal.getClass())){
            SecurityUser user = (SecurityUser) principal;
            return LoginUser.of(user.getUsername(), user.getEmail(), user.getPicture());
        } else {
            User user = (User) principal;
            return LoginUser.of(user.getUsername(), null,null);
        }
    }

}
