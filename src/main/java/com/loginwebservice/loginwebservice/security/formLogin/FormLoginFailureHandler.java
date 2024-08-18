package com.loginwebservice.loginwebservice.security.formLogin;

import com.loginwebservice.loginwebservice.security.formLogin.service.LoginFailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

import static com.loginwebservice.loginwebservice.security.config.SecurityConfig.USERNAME_PARAMETER;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
@RequiredArgsConstructor
public class FormLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    
    private final LoginFailService loginFailService;

    @Override
    public void onAuthenticationFailure(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException exception
    ) throws IOException, ServletException {
        String loginId = request.getParameter(USERNAME_PARAMETER);
        loginFailService.alertLoginFailTo(loginId);
        String errorMessage = Failure.findErrorMessage(exception.getClass());
        log.info("[FormLoginFailureHandler] : FormLogin Fail, message = {}",errorMessage);
        String encodedErrorMessage = URLEncoder.encode(errorMessage, UTF_8);
        setDefaultFailureUrl("/login?errorMessage="+encodedErrorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }

}
