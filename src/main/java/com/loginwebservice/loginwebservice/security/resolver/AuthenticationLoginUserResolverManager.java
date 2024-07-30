package com.loginwebservice.loginwebservice.security.resolver;

import com.loginwebservice.loginwebservice.security.resolver.dto.LoginUser;
import com.loginwebservice.loginwebservice.security.resolver.loginUserProvider.AuthenticationLoginUserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AuthenticationLoginUserResolverManager {

    private final static List<AuthenticationLoginUserProvider> PROVIDERS = new ArrayList<>();

    public void addProvider(AuthenticationLoginUserProvider provider){
        PROVIDERS.add(provider);
    }

    public LoginUser resolve(Authentication authentication){
        for (AuthenticationLoginUserProvider provider : PROVIDERS) {
            if(!provider.supports(authentication)){
                continue;
            }
            log.info("{} supports type of {}",provider.getClass(),authentication.getClass());
            LoginUser result = provider.resolve(authentication);
            if(result == null){
                throw new IllegalStateException(provider.getClass() + "don't properly supports type of" + authentication.getClass());
            }
            return result;
        }

        throw new IllegalStateException("No Provider supports this" + authentication);
    }

}
