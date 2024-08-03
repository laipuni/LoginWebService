package com.loginwebservice.loginwebservice.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncodeService {

    private final PasswordEncoder passwordEncoder;

    public String encodePassword(final String password){
        return passwordEncoder.encode(password);
    }

}
