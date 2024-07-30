package com.loginwebservice.loginwebservice.security.resolver.loginUserProvider;

import com.loginwebservice.loginwebservice.security.resolver.dto.LoginUser;
import org.springframework.security.core.Authentication;

public interface AuthenticationLoginUserProvider {

    boolean supports(Authentication authentication);

    LoginUser resolve(Authentication authentication);

}
