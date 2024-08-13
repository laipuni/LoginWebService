package com.loginwebservice.loginwebservice.jpa.config;

import com.loginwebservice.loginwebservice.jpa.AuditorAwareImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

    @Autowired
    HttpSession httpSession;

    @Bean
    public AuditorAware<Long> auditorProvider(){
        return new AuditorAwareImpl(httpSession);
    }

}
