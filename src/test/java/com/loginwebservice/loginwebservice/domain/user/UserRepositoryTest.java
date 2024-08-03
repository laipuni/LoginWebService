package com.loginwebservice.loginwebservice.domain.user;

import com.loginwebservice.loginwebservice.IntegrationTest;
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


}