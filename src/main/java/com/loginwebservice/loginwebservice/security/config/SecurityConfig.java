package com.loginwebservice.loginwebservice.security.config;

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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers("/logout","/login")
                )
                .authorizeHttpRequests((authorize)-> authorize
                        .requestMatchers(
                                "/login/**", "/users/join",
                                "/profile","/logout/**",
                                "/error","/css/**"
                        )
                        .permitAll()
                )
                .authorizeHttpRequests((authorize)-> authorize
                        .anyRequest().authenticated()
                )
                .formLogin(
                        (login)-> login
                                .loginPage("/login")
                                .successForwardUrl("/")
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
                .build();
    }

}
