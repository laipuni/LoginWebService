package com.loginwebservice.loginwebservice.domain.user;

import com.loginwebservice.loginwebservice.Email.EmailService;
import com.loginwebservice.loginwebservice.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


class UserHelpServiceTest extends IntegrationTest {

    public static final String EXPECTED_EMAIL = "email@email.com";

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserHelpService userHelpService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @MockBean
    EmailService emailService;

    @BeforeEach
    void tearUp(){
        redisTemplate.delete(EXPECTED_EMAIL);
    }

    @DisplayName("아이디 찾기 요청을 했을 때, 요청 받은 이메일을 가진 유저를 찾을 경우 에러가 발생한다.")
    @Test
    void helpUserIdWithNotFoundEmail(){
        //given
        String notFoundEmail = EXPECTED_EMAIL;
        String name = "김사자";

        Mockito.when(emailService.sendEmail(Mockito.anyString(),Mockito.anyString(),Mockito.anyString()))
                .thenReturn(true);

        //when
        //then
        assertThatThrownBy(() -> userHelpService.helpUserId(name,notFoundEmail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("입력하신 정보가 일치하는 유저는 존재하지 않습니다.");
    }

    @DisplayName("아이디 찾기 인증 코드를 받았을 때, 아이디 찾기 인증 코드가 유효하지 않을 경우 에러가 발생한다.")
    @Test
    void verifyHelpUserIdAuthCodeWithNotFoundAuthCode(){
        //given
        String expectedEmail = EXPECTED_EMAIL;
        String expectedAuthCode = "authcode";
        String expectedName = "김사자";
        //when
        //then
        assertThatThrownBy(()->
                userHelpService.verifyHelpUserIdAuthCode(expectedAuthCode,expectedName,expectedEmail)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("인증 요청을 다시 요청해주십시오.");

    }

    @DisplayName("아이디 찾기 인증 코드를 받았을 때, 아이디 찾기 인증 코드가 유효하지 않을 경우 에러가 발생한다.")
    @Test
    void verifyHelpUserIdAuthCodeWithWrongAuthCode(){
        //given
        String expectedEmail = EXPECTED_EMAIL;
        String expectedAuthCode = "authcode";
        String wrongAuthCode = "wrongauthcode";
        String expectedName = "김사자";
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(expectedEmail,expectedAuthCode);
        //when
        //then
        assertThatThrownBy(()->
                userHelpService.verifyHelpUserIdAuthCode(wrongAuthCode,expectedName,expectedEmail)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("인증 코드를 잘못 작성하셨습니다. 입력한 정보가 맞는지 다시 확인해주세요.");
    }

    @DisplayName("아이디 찾기 인증 코드를 받았을 때, 조회할 이름과 이메일과 일치하는 유저가 존재하지 않을 경우 에러가 발생한다.")
    @Test
    void verifyHelpUserIdAuthCodeWithNotFoundUser(){
        //given
        String expectedEmail = EXPECTED_EMAIL;
        String expectedAuthCode = "authcode";
        String expectedName = "김사자";
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(expectedEmail,expectedAuthCode);

        //when
        //then
        assertThatThrownBy(()->
                userHelpService.verifyHelpUserIdAuthCode(expectedAuthCode,expectedName,expectedEmail)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("해당 정보와 일치하는 유저는 존재하지 않습니다.");
    }
}