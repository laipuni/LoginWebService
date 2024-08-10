package com.loginwebservice.loginwebservice.domain.user;

import com.loginwebservice.loginwebservice.Email.AuthCodeUtils;
import com.loginwebservice.loginwebservice.Email.EmailService;
import com.loginwebservice.loginwebservice.domain.user.response.UserIdVerifyAuthCodeResponse;
import com.loginwebservice.loginwebservice.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserHelpService {

    @Value("${spring.mail.properties.mail.smtp.authExpireTime}")
    private long authExpireTime;

    private final RedisService redisService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public void helpUserId(final String name,final String email){
        //입력한 정보가 일치하지 않을 경우 예외 발생
        if(!userRepository.existsUserByEmailAndName(email,name)){
            throw new IllegalArgumentException("입력하신 정보가 일치하는 유저는 존재하지 않습니다.");
        }
        //이전 인증 코드가 남아있을 경우 삭제
        if(!redisService.existData(email)){
            redisService.deleteData(email);
        }

        String title = "인증 번호입니다.";
        String authCode = AuthCodeUtils.createAuthCode();
        redisService.setDataExpire(email,authCode, authExpireTime);
        emailService.sendEmail(email, authCode,title);
    }

    public UserIdVerifyAuthCodeResponse verifyHelpUserIdAuthCode(final String authCode, final String name, final String email){
        String findAuthCode = redisService.getData(email);
        if(findAuthCode == null){
            throw new IllegalArgumentException("인증 요청을 다시 요청해주십시오.");
        }
        if(!findAuthCode.equals(authCode)){
            throw new IllegalArgumentException("인증 코드를 잘못 작성하셨습니다. 입력한 정보가 맞는지 다시 확인해주세요.");
        }
        UserIdVerifyAuthCodeResponse response = userRepository.findByNameAndEmail(name, email)
                .orElseThrow(() -> new IllegalArgumentException("해당 정보와 일치하는 유저는 존재하지 않습니다."));
        redisService.deleteData(email);
        return response;
    }
}
