package com.loginwebservice.loginwebservice.domain.user;

import com.loginwebservice.loginwebservice.IntegrationTest;
import com.loginwebservice.loginwebservice.domain.user.request.UserAddRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class UserServiceTest extends IntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @BeforeEach
    void tearUp(){
        userRepository.deleteAllInBatch();
    }

    @DisplayName("회원가입 요청을 받아 회원을 등록한다.")
    @Test
    void join(){
        //given
        String expectedLoginId = "아이디";
        String expectedName = "라이푸니";
        String expectedPassword = "비밀번호";
        String expectedUserName = "라이푸니";
        String expectedEmail = "email@gmail.com";
        UserAddRequest request = UserAddRequest.builder()
                .loginId(expectedLoginId)
                .password(expectedPassword)
                .userName(expectedUserName)
                .name(expectedName)
                .email(expectedEmail)
                .build();
        //when
        userService.join(request);
        List<User> users = userRepository.findAll();
        //then
        assertThat(users).hasSize(1)
                .extracting("name","userName","loginId","password","email")
                .containsExactly(
                        tuple(expectedName,expectedUserName,expectedLoginId,expectedPassword,expectedEmail)
                );
    }


    @DisplayName("회원가입 요청을 받았을 때, 중복된 유저가 존재할 경우 에러가 발생한다.")
    @Test
    void joinWithDuplicatedUser(){
        //given
        String expectedLoginId = "아이디";
        String expectedName = "라이푸니";
        String expectedPassword = "비밀번호";
        String expectedUserName = "라이푸니";
        String expectedEmail = "email@gmail.com";

        User user = User.builder()
                .loginId(expectedLoginId)
                .password(expectedPassword)
                .email(expectedEmail)
                .name(expectedName)
                .userName(expectedUserName)
                .role(Role.GUEST)
                .build();

        userRepository.save(user);

        UserAddRequest request = UserAddRequest.builder()
                .loginId(expectedLoginId)
                .password(expectedPassword)
                .name(expectedName)
                .userName(expectedUserName)
                .email(expectedEmail)
                .build();
        //when
        //then
        assertThatThrownBy(()->userService.join(request))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}