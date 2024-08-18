package com.loginwebservice.loginwebservice.security.formLogin.service;

import com.loginwebservice.loginwebservice.Email.EmailService;
import com.loginwebservice.loginwebservice.domain.user.User;
import com.loginwebservice.loginwebservice.domain.user.UserRepository;
import com.loginwebservice.loginwebservice.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginFailService {

    public static final String LOGIN_FAIL_COUNT_TOKEN = "[LoginCount]";
    public static final String LOGIN_LOCK = "[LoginLock]";

    @Value("${spring.login.Fail.LockExpiredTime}")
    private long LoginFailLockExpiredTime;
    @Value("${spring.login.Fail.countExpiredTime}")
    private long LoginFailCountExpiredTime;
    @Value("${spring.login.Fail.count}")
    private int LoginFailCount;

    private final RedisService redisService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public void alertLoginFailTo(final String loginId) {
        //해당 아이디가 로그인 락이 걸려 있다면
        if(redisService.existData(createLoginLockKeyBy(loginId))){
            return;
        }
        String loginFailNumber = getLoginFailNumberBy(loginId);
        String incrLoginFailNum = incrementLoginFailNum(loginFailNumber);
        if(isHigherThanLoginFailCondition(incrLoginFailNum) ){
            log.info("[{}] : Fail higher than Condition! Must have Authentication!", loginId);
            User user = userRepository.findUserByLoginId(loginId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));
            //해당 계정의 이메일로 계정이 최대 로그인 실패 횟수를 넘어 메일 전송
            emailService.sendEmail(
                    user.getEmail(),"mail/loginFailLockMailForm", Map.of("date",LocalDate.now().toString()),createTitle()
            );
            redisService.deleteData(createLoginFailCountKeyBy(loginId));
            redisService.setDataExpire(createLoginLockKeyBy(loginId),incrLoginFailNum,LoginFailLockExpiredTime);
        } else{
            //로그인 실패기록을 redis에 설정한 로그인 실패 토큰 유효시간 만큼 저장
            redisService.setDataExpire(
                    createLoginFailCountKeyBy(loginId),incrLoginFailNum,LoginFailCountExpiredTime
            );
        }
    }

    private String createTitle() {
        return  "귀하의 계정이 로그인 실패 횟수를 초과했습니다.";
    }

    private boolean isHigherThanLoginFailCondition(final String loginFailNumber) {
        return Integer.parseInt(loginFailNumber) >= LoginFailCount;
    }

    private String incrementLoginFailNum(final String loginFailNumber) {
        return String.valueOf(Integer.parseInt(loginFailNumber) + 1);
    }

    private String getLoginFailNumberBy(final String loginId) {
        String loginFailCountKey = createLoginFailCountKeyBy(loginId);
        if(redisService.existData(loginFailCountKey)){
            String loginFailNumber = redisService.getData(loginFailCountKey);
            redisService.deleteData(loginFailCountKey);
            return loginFailNumber;
        }
        return "0";
    }

    private static String createLoginFailCountKeyBy(final String loginId) {
        return LOGIN_FAIL_COUNT_TOKEN + loginId;
    }

    private static String createLoginLockKeyBy(final String loginId){
        return LOGIN_LOCK + loginId;
    }

    public boolean hasLoginLock(final String loginId){
        return redisService.existData(createLoginLockKeyBy(loginId));
    }
    public void resetLoginFailCount(final String loginId){
        redisService.deleteData(createLoginFailCountKeyBy(loginId));
    }
}
