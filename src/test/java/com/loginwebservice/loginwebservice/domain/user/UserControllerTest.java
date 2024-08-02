package com.loginwebservice.loginwebservice.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loginwebservice.loginwebservice.domain.user.request.UserAddRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("회원가입 화면을 반환한다.")
    @Test
    void JoinForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/join").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/userAddForm"));
    }

    @DisplayName("회원가입 요청을 받았을 때, 적절한 회원가입 요청일 경우 회원가입 결과를 응답한다.")
    @Test
    void join() throws Exception {
        //given
        UserAddRequest request = UserAddRequest.builder()
                .loginId("abcde123")
                .password("Abcdefg1!")
                .email("email@laipuni.com")
                .name("라이푸니")
                .userName("라이푸니")
                .build();
        //when
        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/join")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("loginId", request.getLoginId())
                                .param("password",request.getPassword())
                                .param("email", request.getEmail())
                                .param("name",request.getName())
                                .param("userName",request.getUserName())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/join/success"));
    }

    @DisplayName("회원가입 요청을 받았을 때, 아이디에 대문자가 있을 경우 에러가 발생한다.")
    @Test
    void joinWithExistUpperAlphabetInLoginId() throws Exception {
        //given
        UserAddRequest request = UserAddRequest.builder()
                .loginId("NOTUPPER123")
                .password("Abcdefg1!")
                .email("email@laipuni.com")
                .name("라이푸니")
                .userName("라이푸니")
                .build();
        //when
        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/join")
                                .with(csrf())
                                .param("loginId", request.getLoginId())
                                .param("password",request.getPassword())
                                .param("email", request.getEmail())
                                .param("name",request.getName())
                                .param("userName",request.getUserName())
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/user/userAddForm"));
    }

    @DisplayName("회원가입 요청을 받았을 때, 아이디에 특수 기호가 있을 경우 에러가 발생한다.")
    @Test
    void joinWithExistSpecialCharacterInLoginId() throws Exception {
        //given
        UserAddRequest request = UserAddRequest.builder()
                .loginId("ABCE~!")
                .password("Abcdefg1!")
                .email("email@laipuni.com")
                .name("라이푸니")
                .userName("라이푸니")
                .build();
        //when
        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/join")
                                .with(csrf())
                                .param("loginId", request.getLoginId())
                                .param("password",request.getPassword())
                                .param("email", request.getEmail())
                                .param("name",request.getName())
                                .param("userName",request.getUserName())
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/user/userAddForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("user"));
    }

    @DisplayName("회원가입 요청을 받았을 때,  아이디가 5자 미만일 경우 에러가 발생한다.")
    @Test
    void joinLoginIdShorterThanFive() throws Exception {
        //given
        UserAddRequest request = UserAddRequest.builder()
                .loginId(" ")
                .password("Abcdefg1!")
                .email("email@laipuni.com")
                .name("라이푸니")
                .userName("라이푸니")
                .build();
        //when
        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/join")
                                .with(csrf())
                                .param("loginId", request.getLoginId())
                                .param("password",request.getPassword())
                                .param("email", request.getEmail())
                                .param("name",request.getName())
                                .param("userName",request.getUserName())
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/user/userAddForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("user"));

    }


    @DisplayName("회원가입 요청을 받았을 때, 아이디가 12자 초과일 경우 에러가 발생한다.")
    @Test
    void joinLoginIdLongerThanTwelve() throws Exception {
        //given
        UserAddRequest request = UserAddRequest.builder()
                .loginId("123456789101112")
                .password("Abcdefg1!")
                .email("email@laipuni.com")
                .name("라이푸니")
                .userName("라이푸니")
                .build();
        //when
        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/join")
                                .with(csrf())
                                .param("loginId", request.getLoginId())
                                .param("password",request.getPassword())
                                .param("email", request.getEmail())
                                .param("name",request.getName())
                                .param("userName",request.getUserName())
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/user/userAddForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("user"));

    }

    @DisplayName("회원가입 요청을 받았을 때, 비밀번호가 8자 미만일 경우 에러가 발생한다.")
    @Test
    void joinWithPasswordShorterThanEight() throws Exception {
        //given
        UserAddRequest request = UserAddRequest.builder()
                .loginId("abcde123")
                .password("Aa!1")
                .email("email@laipuni.com")
                .name("라이푸니")
                .userName("라이푸니")
                .build();
        //when
        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/join")
                                .with(csrf())
                                .param("loginId", request.getLoginId())
                                .param("password",request.getPassword())
                                .param("email", request.getEmail())
                                .param("name",request.getName())
                                .param("userName",request.getUserName())
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/user/userAddForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("user"));

    }

    @DisplayName("회원가입 요청을 받았을 때, 비밀번호가 15자 초과일 경우 에러가 발생한다.")
    @Test
    void joinWithPasswordLongerThanFifteen() throws Exception {
        //given
        UserAddRequest request = UserAddRequest.builder()
                .loginId("abcde123")
                .password("123456789101112A!1")
                .email("email@laipuni.com")
                .name("라이푸니")
                .userName("라이푸니")
                .build();

        //when
        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/join")
                                .with(csrf())
                                .param("loginId", request.getLoginId())
                                .param("password",request.getPassword())
                                .param("email", request.getEmail())
                                .param("name",request.getName())
                                .param("userName",request.getUserName())
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/user/userAddForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("user"));

    }

    @DisplayName("회원가입 요청을 받았을 때, 비밀번호가 특수문자를 1개 미만일 경우 에러가 발생한다.")
    @Test
    void joinWithNoSpecialCharacterInPassword() throws Exception {
        //given
        UserAddRequest request = UserAddRequest.builder()
                .loginId("abcde123")
                .password("Abcdefg1")
                .email("email@laipuni.com")
                .name("라이푸니")
                .userName("라이푸니")
                .build();

        //when
        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/join")
                                .with(csrf())
                                .param("loginId", request.getLoginId())
                                .param("password",request.getPassword())
                                .param("email", request.getEmail())
                                .param("name",request.getName())
                                .param("userName",request.getUserName())
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/user/userAddForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("user"));


    }

    @DisplayName("회원가입 요청을 받았을 때, 비밀번호가 대문자를 1개 미만일 경우 에러가 발생한다.")
    @Test
    void joinWithNoUpperCharacterInPassword() throws Exception {
        //given
        UserAddRequest request = UserAddRequest.builder()
                .loginId("abcde123")
                .password("abcdefg1!")
                .email("email@laipuni.com")
                .name("라이푸니")
                .userName("라이푸니")
                .build();

        //when
        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/join")
                                .with(csrf())
                                .param("loginId", request.getLoginId())
                                .param("password",request.getPassword())
                                .param("email", request.getEmail())
                                .param("name",request.getName())
                                .param("userName",request.getUserName())
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/user/userAddForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("user"));


    }

    @DisplayName("회원가입 요청을 받았을 때, 비밀번호가 소문자를 1개 미만일 경우 에러가 발생한다.")
    @Test
    void joinWithNoSmallCharacterInPassword() throws Exception {
        //given
        UserAddRequest request = UserAddRequest.builder()
                .loginId("abcde123")
                .password("ABCDEFG1!")
                .email("email@laipuni.com")
                .name("라이푸니")
                .userName("라이푸니")
                .build();

        //when
        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/join")
                                .with(csrf())
                                .param("loginId", request.getLoginId())
                                .param("password",request.getPassword())
                                .param("email", request.getEmail())
                                .param("name",request.getName())
                                .param("userName",request.getUserName())
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/user/userAddForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("user"));


    }

    @DisplayName("회원가입 요청을 받았을 때, 비밀번호가 숫자를 1개 미만일 경우 에러가 발생한다.")
    @Test
    void joinWithNoNumberInPassword() throws Exception {
        //given
        UserAddRequest request = UserAddRequest.builder()
                .loginId("abcde123")
                .password("Abcdefg!")
                .email("email@laipuni.com")
                .name("라이푸니")
                .userName("라이푸니")
                .build();

        //when
        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/join")
                                .with(csrf())
                                .param("loginId", request.getLoginId())
                                .param("password",request.getPassword())
                                .param("email", request.getEmail())
                                .param("name",request.getName())
                                .param("userName",request.getUserName())
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/user/userAddForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("user"));


    }

    @DisplayName("회원가입 요청을 받았을 때, 이메일 패턴에 맞지 않는 경우 에러가 발생한다.")
    @Test
    void joinWithInvalidEmail() throws Exception {
        //given
        UserAddRequest request = UserAddRequest.builder()
                .loginId("abcde123")
                .password("Abcdefg1!")
                .email("NotEmail")
                .name("라이푸니")
                .userName("라이푸니")
                .build();

        //when
        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/join")
                                .with(csrf())
                                .param("loginId", request.getLoginId())
                                .param("password",request.getPassword())
                                .param("email", request.getEmail())
                                .param("name",request.getName())
                                .param("userName",request.getUserName())
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/user/userAddForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("user"));
    }


    @DisplayName("회원가입 요청을 받았을 때, 닉네임이 없을 경우 에러가 발생한다.")
    @Test
    void joinWithBlankUserName() throws Exception {
        //given
        UserAddRequest request = UserAddRequest.builder()
                .loginId("abcde123")
                .password("Abcdefg1!")
                .email("email@laipuni.com")
                .name("라이푸니")
                .userName(" ")
                .build();

        //when
        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/join")
                                .with(csrf())
                                .param("loginId", request.getLoginId())
                                .param("password",request.getPassword())
                                .param("email", request.getEmail())
                                .param("name",request.getName())
                                .param("userName",request.getUserName())
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/user/userAddForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("user"));


    }

    @DisplayName("회원가입 요청을 받았을 때, 이름이 없을 경우 에러가 발생한다.")
    @Test
    void joinWithBlankName() throws Exception {
        //given
        UserAddRequest request = UserAddRequest.builder()
                .loginId("abcde123")
                .password("Abcdefg1!")
                .email("email@laipuni.com")
                .name(" ")
                .userName("라이푸니")
                .build();

        

        //when
        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/join")
                                .with(csrf())
                                .param("loginId", request.getLoginId())
                                .param("password",request.getPassword())
                                .param("email", request.getEmail())
                                .param("name",request.getName())
                                .param("userName",request.getUserName())
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/user/userAddForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("user"));

    }
}