package com.loginwebservice.loginwebservice.security.formLogin.provider;

import com.loginwebservice.loginwebservice.security.formLogin.exception.LoginLockException;
import com.loginwebservice.loginwebservice.security.formLogin.service.CustomUserDetailService;
import com.loginwebservice.loginwebservice.security.formLogin.service.LoginFailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FormAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;
    private final LoginFailService loginFailService;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        String loginId = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        if(loginFailService.hasLoginLock(loginId)){
            throw new LoginLockException("해당 계정은 5번이상 로그인 시도 했습니다.");
        }

        UserDetails user = userDetailService.loadUserByUsername(loginId);

        if(!passwordEncoder.matches(password,user.getPassword())){
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
