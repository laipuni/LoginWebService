package com.loginwebservice.loginwebservice.security.formLogin.service;

import com.loginwebservice.loginwebservice.Email.EmailService;
import com.loginwebservice.loginwebservice.domain.user.User;
import com.loginwebservice.loginwebservice.domain.user.UserRepository;
import com.loginwebservice.loginwebservice.redis.RedisRepository;
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
    public static final String LOGIN_LOCK_HTML_PATH = "mail/loginFailLockMailForm";

    @Value("${spring.login.Fail.LockExpiredTime}")
    private long LoginFailLockExpiredTime;
    @Value("${spring.login.Fail.countExpiredTime}")
    private long LoginFailCountExpiredTime;
    @Value("${spring.login.Fail.count}")
    private int LoginFailCount;

    private final RedisRepository redisRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public void alertLoginFailTo(final String loginId) {
        //해당 아이디가 로그인 락이 걸려 있다면 아무것도 하지 않고 종료
        if(redisRepository.existData(createLoginLockKeyBy(loginId))){
            return;
        }
        String loginFailNumber = getLoginFailNumberBy(loginId);
        String incrLoginFailNum = incrementLoginFailNum(loginFailNumber);
        if(isHigherThanLoginFailCondition(incrLoginFailNum) ){
            //해당 계정의 이메일로 계정이 최대 로그인 실패 횟수를 넘어 메일 전송
            log.info("[{}] : Fail higher than Condition! Must have Authentication!", loginId);
            User user = userRepository.findUserByLoginId(loginId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));
            emailService.sendEmail(
                    user.getEmail(),LOGIN_LOCK_HTML_PATH, Map.of("date",LocalDate.now().toString()),createTitle()
            );
            redisRepository.deleteData(createLoginFailCountKeyBy(loginId));
            redisRepository.setDataExpire(createLoginLockKeyBy(loginId),incrLoginFailNum,LoginFailLockExpiredTime);
        } else{
            //로그인 실패기록을 redis에 설정한 로그인 실패 토큰 유효시간 만큼 저장
            redisRepository.setDataExpire(
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
        if(redisRepository.existData(loginFailCountKey)){
            String loginFailNumber = redisRepository.getData(loginFailCountKey);
            redisRepository.deleteData(loginFailCountKey);
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
        return redisRepository.existData(createLoginLockKeyBy(loginId));
    }
    public void resetLoginFailCount(final String loginId){
        redisRepository.deleteData(createLoginFailCountKeyBy(loginId));
    }
}
