package com.loginwebservice.loginwebservice.domain.user;

import com.loginwebservice.loginwebservice.IntegrationTest;
import com.loginwebservice.loginwebservice.domain.user.response.UserIdVerifyAuthCodeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


class UserRepositoryTest extends IntegrationTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void tearUp(){
        userRepository.deleteAllInBatch();
    }

    @DisplayName("email과 동일한 유저를 조회한다.")
    @Test
    void findUserByEmail(){
        //given
        String expectedName = "라이푸니";
        String expectedEmail = "2tgb02023@gmail.com";
        User user = User.builder()
                .name(expectedName)
                .email(expectedEmail)
                .role(Role.USER)
                .build();
        userRepository.save(user);
        //when
        User findUser = userRepository.findUserByEmail(expectedEmail).get();
        //then
        assertThat(findUser)
                .extracting("name","email")
                .containsExactly(expectedName,expectedEmail);
    }

    @DisplayName("주어진 닉네임과 같은 유저가 존재하는지 확인한다.")
    @Test
    void existsUserByUserName(){
        //given
        String duplicateUserName = "중복된 닉네임";
        User user = User.builder()
                .name("이름")
                .userName(duplicateUserName)
                .loginId("아이디")
                .role(Role.USER)
                .password("비밀번호")
                .email("email@gmail.com")
                .build();
        userRepository.save(user);

        //when
        boolean result = userRepository.existsUserByUserName(duplicateUserName);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("주어진 아이디와 같은 유저가 존재하는지 확인한다.")
    @Test
    void existsUserByLoginId(){
        //given
        String duplicateLoginId = "중복된 아이디";
        User user = User.builder()
                .name("이름")
                .userName("중복된 닉네임")
                .loginId(duplicateLoginId)
                .role(Role.USER)
                .password("비밀번호")
                .email("email@gmail.com")
                .build();
        userRepository.save(user);

        //when
        boolean result = userRepository.existsUserByLoginId(duplicateLoginId);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("주어진 아이디와 같은 유저가 존재하는지 확인한다.")
    @Test
    void findUserByLoginId(){
        //given
        String expectedLoginId = "아이디";
        User user = User.builder()
                .name("이름")
                .userName("닉네임")
                .loginId(expectedLoginId)
                .role(Role.USER)
                .password("비밀번호")
                .email("email@gmail.com")
                .build();
        userRepository.save(user);

        //when
        User findUser = userRepository.findUserByLoginId(expectedLoginId).get();

        //then
        assertThat(findUser.getLoginId()).isEqualTo(expectedLoginId);
    }

    @DisplayName("이름과 메일과 같은 유저가 존재하는지 조회한다.")
    @Test
    void existsUserByEmailAndName(){
        //given
        String expectedName = "김사자";
        String expectedMail = "김사자씨메일@mail.com";

        User user = User.builder()
                .name(expectedName)
                .loginId("loginId")
                .password("password")
                .email(expectedMail)
                .picture(null)
                .role(Role.USER)
                .build();
        userRepository.save(user);
        //when
        boolean result = userRepository.existsUserByEmailAndName(expectedMail, expectedName);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("이름과 메일과 같은 유저의 아이디를 Dto에 담아 조회한다")
    @Test
    void findByEmailAndName(){
        //given
        String expectedName = "김사자";
        String expectedMail = "김사자씨메일@mail.com";
        String expectedLoginId = "김사자씨아이디";

        User user = User.builder()
                .name(expectedName)
                .loginId(expectedLoginId)
                .password("password")
                .email(expectedMail)
                .picture(null)
                .role(Role.USER)
                .build();
        userRepository.save(user);

        //when
        UserIdVerifyAuthCodeResponse result = userRepository.findByNameAndEmail(expectedName, expectedMail).get();

        //then
        assertThat(result.getLoginId()).isEqualTo(expectedLoginId);
    }
}