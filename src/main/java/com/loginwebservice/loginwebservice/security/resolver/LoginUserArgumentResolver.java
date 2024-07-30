package com.loginwebservice.loginwebservice.security.resolver;

import com.loginwebservice.loginwebservice.security.annotation.UserInfo;
import com.loginwebservice.loginwebservice.security.resolver.dto.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    private final AuthenticationLoginUserResolverManager manager;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        boolean isLoginUser = LoginUser.class.equals(parameter.getParameterType());
        boolean isUserInfo = parameter.getParameterAnnotation(UserInfo.class) != null;
        return isLoginUser && isUserInfo;
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {
        //SecurityContext에 담긴 Authentication을 조회
        Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
        //각각의 방식에 지원하는 provider를 찾아 loginUser로 담아줌
        LoginUser result = manager.resolve(authentication);
        if(result == null){
            throw new IllegalStateException("No provider supports type of " + authentication.getClass());
        }
        return result;
    }
}
