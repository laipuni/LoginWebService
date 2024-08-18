package com.loginwebservice.loginwebservice.security.formLogin;

import com.loginwebservice.loginwebservice.security.formLogin.service.LoginFailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.loginwebservice.loginwebservice.security.config.SecurityConfig.USERNAME_PARAMETER;

@Slf4j
@Component
@RequiredArgsConstructor
public class FormLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String DEFAULT_URL = "/";
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private final LoginFailService loginFailService;
    @Override
    public void onAuthenticationSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication
    ) throws IOException, ServletException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        //로그인 성공시 로그인 시도 횟수 초기화
        loginFailService.resetLoginFailCount(request.getParameter(USERNAME_PARAMETER));
        if(savedRequest != null){
            String redirectUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request,response,redirectUrl);
        } else{
            redirectStrategy.sendRedirect(request,response,DEFAULT_URL);
        }
    }
}
