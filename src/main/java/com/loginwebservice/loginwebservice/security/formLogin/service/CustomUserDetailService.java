package com.loginwebservice.loginwebservice.security.formLogin.service;

import com.loginwebservice.loginwebservice.domain.user.User;
import com.loginwebservice.loginwebservice.domain.user.UserRepository;
import com.loginwebservice.loginwebservice.security.formLogin.domain.SecurityUser;
import com.loginwebservice.loginwebservice.security.formLogin.exception.NotAuthenticationUserException;
import com.loginwebservice.loginwebservice.security.oauth2.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 아이디의 유저가 존재하지 않습니다."));

        if(!user.isAuthenticated()){
            throw new NotAuthenticationUserException("해당 계정은 인증하지 않았습니다. 이메일을 확인해주세요");
        }

        httpSession.setAttribute("user",new SessionUser(user));

        return new SecurityUser(user);
    }
}
