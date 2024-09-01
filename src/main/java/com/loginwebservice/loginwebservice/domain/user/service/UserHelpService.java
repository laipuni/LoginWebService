package com.loginwebservice.loginwebservice.domain.user.service;

import com.loginwebservice.loginwebservice.Email.AuthCodeUtils;
import com.loginwebservice.loginwebservice.Email.EmailService;
import com.loginwebservice.loginwebservice.domain.user.User;
import com.loginwebservice.loginwebservice.domain.user.UserRepository;
import com.loginwebservice.loginwebservice.domain.user.response.LoginIdValidationResponse;
import com.loginwebservice.loginwebservice.domain.user.response.PasswordAuthCodeValidResponse;
import com.loginwebservice.loginwebservice.domain.user.response.LoginIdSearchResponse;
import com.loginwebservice.loginwebservice.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserHelpService {

    @Value("${spring.mail.properties.mail.smtp.authExpireTime}")
    private long authExpireTime;

    @Value("${spring.help.token.expireTime}")
    private long tokenHelpExpireTime;

    private final PasswordEncoder passwordEncoder;
    private final RedisRepository redisRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public void sendHelpLoginIdAuthMail(final String name, final String email){
        //입력한 정보가 일치하지 않을 경우 예외 발생
        if(!userRepository.existsUserByEmailAndName(email,name)){
            throw new IllegalArgumentException("입력하신 정보가 일치하는 유저는 존재하지 않습니다.");
        }
        //이전 인증 코드가 남아있을 경우 삭제
        if(!redisRepository.existData(email)){
            redisRepository.deleteData(email);
        }

        String title = "[아이디] 인증 번호입니다.";
        String authCode = AuthCodeUtils.createAuthCode();
        redisRepository.setDataExpire(email,authCode, authExpireTime);
        emailService.sendEmail(
                email,"mail/authMailForm",
                Map.of(
                        "authCode",authCode,
                        "subject", "아이디 찾기"
                ),
                title
        );
    }

    public LoginIdValidationResponse validHelpLoginIdAuthCode(final String authCode, final String name, final String email){
        String findAuthCode = redisRepository.getData(email);
        if(findAuthCode == null){
            throw new IllegalArgumentException("인증 요청을 다시 요청해주십시오.");
        }
        if(!findAuthCode.equals(authCode)){
            throw new IllegalArgumentException("인증 코드를 잘못 작성하셨습니다. 입력한 정보가 맞는지 다시 확인해주세요.");
        }
        LoginIdValidationResponse response = userRepository.findLoginIdValidationDtoBy(name, email)
                .orElseThrow(() -> new IllegalArgumentException("해당 정보와 일치하는 유저는 존재하지 않습니다."));
        redisRepository.deleteData(email);
        return response;
    }

    public LoginIdSearchResponse searchLoginId(final String loginId, final String helpToken){
        User user = userRepository.findUserByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        redisRepository.setDataExpire(helpToken, user.getLoginId(), tokenHelpExpireTime);

        return LoginIdSearchResponse.of(true,helpToken);
    }

    public void sendHelpPasswordAuthMail(final String name, final String email){
        //입력한 정보가 일치하지 않을 경우 예외 발생
        if(!userRepository.existsUserByEmailAndName(email,name)){
            throw new IllegalArgumentException("입력하신 정보가 일치하는 유저는 존재하지 않습니다.");
        }

        //이전 인증 코드가 남아있을 경우 삭제
        if(!redisRepository.existData(email)){
            redisRepository.deleteData(email);
        }

        String title = "[비밀번호] 인증 번호입니다.";
        String authCode = AuthCodeUtils.createAuthCode();
        redisRepository.setDataExpire(email,authCode, authExpireTime);
        emailService.sendEmail(
                email,"mail/authMailForm",
                Map.of(
                        "authCode",authCode,
                        "subject", "비밀번호 찾기"
                ),
                title
        );
    }

    public PasswordAuthCodeValidResponse validPasswordAuthCode(final String authCode, final String name, final String email){
        String findAuthCode = redisRepository.getData(email);
        if(findAuthCode == null){
            throw new IllegalArgumentException("인증 요청을 다시 요청해주십시오.");
        }
        if(!findAuthCode.equals(authCode)){
            throw new IllegalArgumentException("인증 코드를 잘못 작성하셨습니다. 입력한 정보가 맞는지 다시 확인해주세요.");
        }
        PasswordAuthCodeValidResponse response = userRepository.findPasswordValidationDtoBy(name, email)
                .orElseThrow(() -> new IllegalArgumentException("해당 정보와 일치하는 유저는 존재하지 않습니다."));
        redisRepository.deleteData(email);
        return response;
    }

    @Transactional
    public void resetPassword(final String token, final String password) {
        String loginId = redisRepository.getData(token);
        if(loginId == null){
            throw new IllegalArgumentException("비밀번호 세션이 만료됐습니다.");
        }
        User user = userRepository.findUserByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않거나 변경할 수 없는 유저입니다."));
        if(passwordEncoder.matches(password, user.getPassword())){
            throw new IllegalArgumentException("이전 비밀번호와 같습니다. 다른 비밀번호로 입력해주세요.");
        }
        String encodedPassword = passwordEncoder.encode(password);
        user.resetPassword(encodedPassword);
    }
}
