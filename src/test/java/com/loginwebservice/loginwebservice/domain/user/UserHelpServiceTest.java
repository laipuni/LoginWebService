package com.loginwebservice.loginwebservice.domain.user;

import com.loginwebservice.loginwebservice.Email.EmailService;
import com.loginwebservice.loginwebservice.IntegrationTest;
import com.loginwebservice.loginwebservice.domain.user.response.LoginIdSearchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class UserHelpServiceTest extends IntegrationTest {

    public static final String EXPECTED_EMAIL = "email@email.com";
    public static final String TOKEN_HELP = "token_help";

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserHelpService userHelpService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @MockBean
    EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void tearUp(){
        redisTemplate.delete(TOKEN_HELP);
        redisTemplate.delete(EXPECTED_EMAIL);
        userRepository.deleteAllInBatch();
    }

    @DisplayName("아이디 찾기 요청을 했을 때, 요청 받은 이메일의 유저가 없을 경우 에러가 발생한다.")
    @Test
    void helpUserIdWithNotFoundEmail(){
        //given
        String notFoundEmail = EXPECTED_EMAIL;
        String name = "김사자";

        Mockito.when(emailService.sendEmail(
                Mockito.anyString(),Mockito.anyString(),
                        Map.of(
                                "authCode",Mockito.anyString(),
                                "subject","아이디 찾기"
                        ),
                        Mockito.anyString()
                ))
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
                userHelpService.validHelpUserIdAuthCode(expectedAuthCode,expectedName,expectedEmail)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("인증 요청을 다시 요청해주십시오.");

    }

    @DisplayName("아이디 찾기 인증 코드를 받았을 때, 아이디 찾기 인증 코드와 다를 경우 에러가 발생한다.")
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
                userHelpService.validHelpUserIdAuthCode(wrongAuthCode,expectedName,expectedEmail)
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
                userHelpService.validHelpUserIdAuthCode(expectedAuthCode,expectedName,expectedEmail)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("해당 정보와 일치하는 유저는 존재하지 않습니다.");
    }

    @DisplayName("비밀번호를 찾을 아이디를 받았을 때, 해당 아이디의 유저가 존재할 경우 비밀번호 찾기 토큰을 생성한다.")
    @Test
    void searchLoginId(){
        //given
        String findLoginId = "아이디";
        User user = User.builder()
                .loginId(findLoginId)
                .userName("김사자")
                .name("김사자")
                .email("email@email.com")
                .picture(null)
                .role(Role.USER)
                .build();
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        userRepository.save(user);
        String key = UUID.randomUUID().toString();

        //when
        LoginIdSearchResponse searchLoginIdResponse = userHelpService.searchLoginId(findLoginId, key);
        String loginId = operations.get(searchLoginIdResponse.getToken());

        //then
        assertThat(loginId).isEqualTo(findLoginId);
    }

    @DisplayName("비밀번호를 찾을 아이디를 받았을 때, 해당 아이디의 유저가 존재하지 않을 경우 에러가 발생한다.")
    @Test
    void searchLoginIdWithNotExistUser(){
        //given
        String findLoginId = "아이디";
        String key = UUID.randomUUID().toString();

        //when
        //then
        assertThatThrownBy(() -> userHelpService.searchLoginId(findLoginId,key))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("해당 유저는 존재하지 않습니다.");

    }


    @DisplayName("비밀번호 찾기 인증코드를 요청할 때, 인증코드가 존재할 경우 이전 인증코드를 삭제한다.")
    @Test
    void helpUserPassword(){
        //given
        String expectedName="김사자";
        String expectedEmail = "email@email.com";
        String beforePasswordAuthCode = "beforeAuthCode";

        User user = User.builder()
                .userName(expectedName)
                .password("비밀번호")
                .role(Role.USER)
                .picture(null)
                .name(expectedName)
                .loginId("아이디")
                .email(expectedEmail)
                .build();
        userRepository.save(user);

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(expectedEmail,beforePasswordAuthCode);

        Mockito.when(emailService.sendEmail(
                        Mockito.anyString(),Mockito.anyString(),
                        Map.of(
                                "authCode",Mockito.anyString(),
                                "subject","비밀번호 찾기"
                        ),
                        Mockito.anyString()
                ))
                .thenReturn(true);
        //when
        userHelpService.helpUserPassword(expectedName,expectedEmail);

        //then
        assertThat(redisTemplate.hasKey(expectedEmail)).isTrue();
        assertThat(operations.get(expectedEmail)).isNotEqualTo(beforePasswordAuthCode);
    }

    @DisplayName("비밀번호 찾기 인증코드를 요청할 때, 이름, 이메일의 유저가 존재하지 않을 경우 에러가 발생한다.")
    @Test
    void helpUserPasswordWithNotExistUser(){
        //given
        String expectedName="김사자";
        String expectedEmail = "email@email.com";

        Mockito.when(emailService.sendEmail(
                        Mockito.anyString(),Mockito.anyString(),
                        Map.of(
                                "authCode",Mockito.anyString(),
                                "subject","비밀번호 찾기"
                        ),
                        Mockito.anyString()
                ))
                .thenReturn(true);
        //when
        //then
        assertThatThrownBy(() ->userHelpService.helpUserPassword(expectedName,expectedEmail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("입력하신 정보가 일치하는 유저는 존재하지 않습니다.");
    }

    @DisplayName("비밀번호 인증 코드를 검증할 때, 인증 코드가 유효하지 않을 경우 에러가 발생한다.")
    @Test
    void verifyPasswordAuthCodeWithExpireAuthCode(){
        //given
        String expectedEmail = EXPECTED_EMAIL;
        String expireAuthCode = "authcode";
        String expectedName = "김사자";
        //when
        //then
        assertThatThrownBy(()->
                userHelpService.validPasswordAuthCode(expireAuthCode,expectedName,expectedEmail)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("인증 요청을 다시 요청해주십시오.");

    }

    @DisplayName("비밀번호 인증 코드를 검증할 때, 인증 코드가 틀렸을 경우 에러가 발생한다.")
    @Test
    void verifyPasswordAuthCodeWithWrongAuthCode(){
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
                userHelpService.validPasswordAuthCode(wrongAuthCode,expectedName,expectedEmail)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("인증 코드를 잘못 작성하셨습니다. 입력한 정보가 맞는지 다시 확인해주세요.");
    }

    @DisplayName("새 비밀번호을 받아 유저의 비밀번호를 재설정한다")
    @Test
    void resetPassword(){
        //given
        String token = TOKEN_HELP;
        String loginId = "loginId";
        String oldPassword = passwordEncoder.encode("oldPassword");
        String newPassword = "newPassword";

        User user = User.builder()
                .userName("김사자")
                .password(oldPassword)
                .email("email@email.com")
                .picture(null)
                .name("김사자")
                .role(Role.USER)
                .loginId(loginId)
                .build();

        userRepository.save(user);

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(token,loginId);

        //when
        userHelpService.resetPassword(token,newPassword);
        List<User> users = userRepository.findAll();

        //then
        assertThat(
                passwordEncoder.matches(newPassword,users.get(0).getPassword())
        ).isTrue();
    }

    @DisplayName("새 비밀번호로 재설정할 때, 비밀번호 찾기 토큰(token_help)이 만료된 경우 에러가 발생한다.")
    @Test
    void resetPasswordWith(){
        //given
        String token = TOKEN_HELP;
        String loginId = "loginId";
        String oldPassword = passwordEncoder.encode("oldPassword");
        String newPassword = "newPassword";

        User user = User.builder()
                .userName("김사자")
                .password(oldPassword)
                .email("email@email.com")
                .picture(null)
                .name("김사자")
                .role(Role.USER)
                .loginId(loginId)
                .build();
        userRepository.save(user);

        //when
        //then
        assertThatThrownBy(() -> userHelpService.resetPassword(token,newPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("비밀번호 세션이 만료됐습니다.");
    }

    @DisplayName("새 비밀번호로 재설정할 때, 유저가 존재하지 않거나 OAuth2방식 로그인 유저일 경우 에러가 발생한다.")
    @Test
    void resetPasswordWithNotFoundUser(){
        //given
        String token = TOKEN_HELP;
        String loginId = "loginId";
        String newPassword = "newPassword";

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(token,loginId);
        //when
        //then
        assertThatThrownBy(() -> userHelpService.resetPassword(token,newPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("해당 유저는 존재하지 않거나 변경할 수 없는 유저입니다.");
    }

    @DisplayName("새 비밀번호로 재설정할 때, 이전 비밀번호와 새 비밀번호가 같은 경우 에러가 발생한다.")
    @Test
    void resetPasswordWithSameOldPassword(){
        //given
        String token = TOKEN_HELP;
        String loginId = "loginId";
        String oldPassword = passwordEncoder.encode("oldPassword");
        String sameOldPassword = "oldPassword";

        User user = User.builder()
                .userName("김사자")
                .password(oldPassword)
                .email("email@email.com")
                .picture(null)
                .name("김사자")
                .role(Role.USER)
                .loginId(loginId)
                .build();
        userRepository.save(user);

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(token,loginId);

        //when
        //then
        assertThatThrownBy(() -> userHelpService.resetPassword(token,sameOldPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("이전 비밀번호와 같습니다. 다른 비밀번호로 입력해주세요.");
    }
}