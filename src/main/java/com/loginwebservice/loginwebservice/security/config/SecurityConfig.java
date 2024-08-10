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
                                "/login/**", "/users/join",
                                "/profile","/logout/**",
                                "/error/**","/css/**",
                                "/users/join/success","/api/users/join/check-login-id",
                                "/api/users/join/check-user-name","/api/users/send-id-auth-code",
                                "/api/users/valid-id-auth-code", "/users/help-id"
                        )
                        .permitAll()
                )
                .authorizeHttpRequests((authorize)-> authorize
                        .anyRequest().authenticated()
                )
                .formLogin(
                        (login)-> login
                                .loginPage("/login")
                                .usernameParameter("loginId")
                                .passwordParameter("password")
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
