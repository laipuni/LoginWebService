package com.loginwebservice.loginwebservice.jpa;

import com.loginwebservice.loginwebservice.security.formLogin.domain.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            log.debug("Not found Authentication");
            return null;
        }

        if(!authentication.isAuthenticated()){
            log.debug("Not Authentication");
            return null;
        }

        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        return Optional.of(user.getUserSeq());
    }
}
