package com.loginwebservice.loginwebservice.domain.user;

import com.loginwebservice.loginwebservice.Email.EmailService;
import com.loginwebservice.loginwebservice.domain.user.request.UserAddServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

    @Value("${spring.domain}")
    public  String domain;

    private final UserRepository userRepository;
    private final UserService userService;
    private final EmailService emailService;

    public void register(final UserAddServiceRequest request){
        userService.join(request);
        sendRegisterAuthEmail(request.getEmail(),request.getLoginId());
    }

    public void sendRegisterAuthEmail(final String email, final String loginId){
        emailService.sendEmail(
                email,
                "mail/registerAuthMailForm",
                Map.of("registerAuthUrl",domain + "/users/" + loginId + "/verify-register"),
                "회원가입 인증 이메일입니다."
        );
    }

    public void sendRegisterAuthEmail(final String loginId){
        User user = userRepository.findUserByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));
        sendRegisterAuthEmail(user.getEmail(), user.getLoginId());
    }


    @Transactional
    public void verifyRegister(final String loginId){
        User user = userRepository.findUserByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));
        user.changeAuthentication();
    }
}
