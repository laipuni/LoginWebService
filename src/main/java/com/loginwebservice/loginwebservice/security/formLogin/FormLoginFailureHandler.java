package com.loginwebservice.loginwebservice.security.formLogin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
public class FormLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException exception
    ) throws IOException, ServletException {
        String errorMessage = Failure.findErrorMessage(exception.getClass());
        log.info("[FormLoginFailureHandler] : FormLogin Fail, message = {}",errorMessage);
        String encodedErrorMessage = URLEncoder.encode(errorMessage, UTF_8);
        setDefaultFailureUrl("/login?errorMessage="+encodedErrorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}
