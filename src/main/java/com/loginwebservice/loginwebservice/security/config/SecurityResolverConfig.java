package com.loginwebservice.loginwebservice.security.config;

import com.loginwebservice.loginwebservice.security.resolver.AuthenticationLoginUserResolverManager;
import com.loginwebservice.loginwebservice.security.resolver.LoginUserArgumentResolver;
import com.loginwebservice.loginwebservice.security.resolver.loginUserProvider.FormLoginUserProvider;
import com.loginwebservice.loginwebservice.security.resolver.loginUserProvider.GoogleLoginUserProvider;
import com.loginwebservice.loginwebservice.security.resolver.loginUserProvider.NaverLoginUserProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class SecurityResolverConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginArgumentResolver());
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }

    @Bean
    private static LoginUserArgumentResolver loginArgumentResolver() {
        return new LoginUserArgumentResolver(authenticationLoginUserResolverManager());
    }

    @Bean
    private static AuthenticationLoginUserResolverManager authenticationLoginUserResolverManager() {
        AuthenticationLoginUserResolverManager manager = new AuthenticationLoginUserResolverManager();
        manager.addProvider(new FormLoginUserProvider());
        manager.addProvider(new GoogleLoginUserProvider());
        manager.addProvider(new NaverLoginUserProvider());
        return manager;
    }
}
