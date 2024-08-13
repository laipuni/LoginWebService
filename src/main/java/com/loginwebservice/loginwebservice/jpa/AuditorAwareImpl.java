package com.loginwebservice.loginwebservice.jpa;

import com.loginwebservice.loginwebservice.security.oauth2.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Slf4j
public class AuditorAwareImpl implements AuditorAware<Long> {

    private HttpSession httpSession;
    public AuditorAwareImpl(final HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @Override
    public Optional<Long> getCurrentAuditor() {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user == null){
            log.debug("Not found Session");
            return null;
        }

        //OAuth2 유저는 principal이 SecurityUser가 아닌 DefaultOAuth2User이다.
        return Optional.of(user.getUserSeq());
    }
}
