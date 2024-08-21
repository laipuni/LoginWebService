package com.loginwebservice.loginwebservice.security.config;

import com.loginwebservice.loginwebservice.security.formLogin.FormAuthenticationProvider;
import com.loginwebservice.loginwebservice.security.formLogin.FormLoginFailureHandler;
import com.loginwebservice.loginwebservice.security.formLogin.FormLoginSuccessHandler;
import com.loginwebservice.loginwebservice.security.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String USERNAME_PARAMETER = "loginId";
    public static final String PASSWORD_PARAMETER = "password";


    private final CustomOAuth2UserService customOAuth2UserService;
    private final FormAuthenticationProvider formAuthenticationProvider;
    private final FormLoginFailureHandler formLoginFailureHandler;
    private final FormLoginSuccessHandler formLoginSuccessHandler;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers("/logout")
                )
                .authorizeHttpRequests((authorize)-> authorize
                        .requestMatchers(
                                "/login/**", "/profile",
                                "/error/**","/css/**", "/users/{loginId}/verify-register"
                        )
                        .permitAll()
                        .requestMatchers(//회원가입 관련 url
                                "/users/join",
                                "/users/join/success",
                                "/api/users/join/check-login-id",
                                "/api/users/join/check-user-name"
                        ).permitAll()
                        .requestMatchers(//아이디 찾기 관련 url
                                "/api/users/send-id-auth-code",
                                "/api/users/valid-id-auth-code", "/users/help-id"
                        ).permitAll()
                        .requestMatchers(//비밀번호 찾기 관련 url
                                "/users/help-password",
                                "/api/users/search-loginId","/api/users/send-password-auth-code",
                                "/api/users/valid-password-auth-code","/api/users/reset-password"
                        ).permitAll()
                )
                .authorizeHttpRequests((authorize)-> authorize
                        .anyRequest().authenticated()
                )
                .formLogin(
                        (login)-> login
                                .loginPage("/login")
                                .usernameParameter(USERNAME_PARAMETER)
                                .passwordParameter(PASSWORD_PARAMETER)
                                .successHandler(formLoginSuccessHandler)
                                .failureHandler(formLoginFailureHandler)
                )
                .oauth2Login((oauth)->oauth
                        .loginPage("/login")
                        .userInfoEndpoint((endPoint)->endPoint
                                .userService(customOAuth2UserService)
                        )
                )
                .logout((logout)->logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies()
                )
                .authenticationProvider(formAuthenticationProvider)
                .build();
    }

}
