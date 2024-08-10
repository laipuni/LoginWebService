package com.loginwebservice.loginwebservice.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loginwebservice.loginwebservice.domain.user.req.UserIdVerifyAuthCodeRequest;
import com.loginwebservice.loginwebservice.domain.user.request.UserIdSendAuthCodeRequest;
import com.loginwebservice.loginwebservice.domain.user.response.UserIdVerifyAuthCodeResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class UserApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    UserHelpService userHelpService;

    @DisplayName("중복 체크할 아이디를 받아 중복된 아이디가 존재하는지 반환한다.")
    @Test
    void isExistSameLoginIdUser() throws Exception {
        //given
        String loginId = "아이디";

        Mockito.when(userService.isExistSameLoginIdUSer(loginId)).thenReturn(Boolean.TRUE);

        //when
        //then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users/join/check-login-id?loginId=" + loginId)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(Boolean.TRUE));
    }

    @DisplayName("중복 체크할 닉네임을 받아 중복된 닉네임이 존재하는지 반환한다.")
    @Test
    void isExistSameUserName() throws Exception {
        //given
        String userName = "닉네임";

        Mockito.when(userService.isExistSameUserName(userName)).thenReturn(Boolean.TRUE);

        //when
        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/users/join/check-user-name?userName=" + userName)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(Boolean.TRUE));
    }


    @DisplayName("아이디 인증 코드 요청시, 이름과 이메일이 적절하게 받았을 경우 결과를 반환한다.")
    @Test
    void sendAuthCode() throws Exception {
        //given
        String expectedName = "김사자";
        String expectedEmail = "email@email.com";

        UserIdSendAuthCodeRequest request = UserIdSendAuthCodeRequest.builder()
                .name(expectedName)
                .email(expectedEmail)
                .build();

        String data = objectMapper.writeValueAsString(request);

        //when
        //then
        mockMvc.perform(
                post("/api/users/send-id-auth-code")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(data)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("아이디 인증 코드 요청시, 이름을 입력하지 않은 경우 에러가 발생한다.")
    @Test
    void sendAuthCodeWithBlankLoginId() throws Exception {
        //given
        String blankName = "";
        String expectedEmail = "email@email.com";

        UserIdSendAuthCodeRequest request = UserIdSendAuthCodeRequest.builder()
                .name(blankName)
                .email(expectedEmail)
                .build();

        String data = objectMapper.writeValueAsString(request);

        //when
        //then
        mockMvc.perform(
                        post("/api/users/send-id-auth-code")
                                .with(csrf())
                                .contentType(APPLICATION_JSON)
                                .content(data)
                )
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("이름 : 해당 입력은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }


    @DisplayName("아이디 인증 코드 요청시, 받은 이메일의 형식에 맞지 않는 경우 에러가 발생한다.")
    @Test
    void sendAuthCodeWithInvalidEmail() throws Exception {
        //given
        String expectedName = "김사자";
        String wrongEmail = "wrongEmail";

        UserIdSendAuthCodeRequest request = UserIdSendAuthCodeRequest.builder()
                .name(expectedName)
                .email(wrongEmail)
                .build();

        String data = objectMapper.writeValueAsString(request);

        //when
        //then
        mockMvc.perform(
                        post("/api/users/send-id-auth-code")
                                .with(csrf())
                                .contentType(APPLICATION_JSON)
                                .content(data)
                )
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("이메일 : 이메일 주소가 정확한지 확인해 주세요."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("아이디 인증 코드 검증 요청시, 이름,이메일,인증 코드 적절하게 받았을 경우 결과를 반환한다.")
    @Test
    void verifyAuthCode() throws Exception {
        //given
        String expectedName = "김사자";
        String expectedEmail = "email@email.com";
        String expectedAuthCode = "123456";
        String mockLoginId = "loginId";

        UserIdVerifyAuthCodeRequest request = UserIdVerifyAuthCodeRequest.builder()
                .name(expectedName)
                .email(expectedEmail)
                .authCode(expectedAuthCode)
                .build();

        String data = objectMapper.writeValueAsString(request);

        UserIdVerifyAuthCodeResponse response = UserIdVerifyAuthCodeResponse.builder()
                .loginId(mockLoginId)
                .build();

        Mockito.when(
                    userHelpService.verifyHelpUserIdAuthCode(expectedAuthCode,expectedName,expectedEmail)
                ).thenReturn(response);

        //when
        //then
        mockMvc.perform(
                        post("/api/users/valid-id-auth-code")
                                .with(csrf())
                                .contentType(APPLICATION_JSON)
                                .content(data)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.loginId").value(mockLoginId));
    }

    @DisplayName("아이디 인증 코드 검증 요청시, 이름  받았을 경우 결과를 반환한다.")
    @Test
    void verifyAuthCodeWithBlankName() throws Exception {
        //given
        String expectedName = " ";
        String expectedEmail = "email@email.com";
        String expectedAuthCode = "123456";

        UserIdVerifyAuthCodeRequest request = UserIdVerifyAuthCodeRequest.builder()
                .name(expectedName)
                .email(expectedEmail)
                .authCode(expectedAuthCode)
                .build();

        String data = objectMapper.writeValueAsString(request);

        //when
        //then
        mockMvc.perform(
                        post("/api/users/valid-id-auth-code")
                                .with(csrf())
                                .contentType(APPLICATION_JSON)
                                .content(data)
                )
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("이름 : 해당 입력은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @DisplayName("아이디 인증 코드 검증 요청시, 받은 이메일의 형식에 맞지 않는 경우 에러가 발생한다.")
    @Test
    void verifyAuthCodeWithInvalidEmail() throws Exception {
        //given
        String expectedName = "김사자";
        String expectedEmail = "wrongEmail";
        String expectedAuthCode = "123456";

        UserIdVerifyAuthCodeRequest request = UserIdVerifyAuthCodeRequest.builder()
                .name(expectedName)
                .email(expectedEmail)
                .authCode(expectedAuthCode)
                .build();

        String data = objectMapper.writeValueAsString(request);

        //when
        //then
        mockMvc.perform(
                        post("/api/users/valid-id-auth-code")
                                .with(csrf())
                                .contentType(APPLICATION_JSON)
                                .content(data)
                )
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("이메일 : 이메일 주소가 정확한지 확인해 주세요."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("아이디 인증 코드 검증 요청시, 인증 코드가 없을 경우 에러가 발생한다.")
    @Test
    void verifyAuthCodeWithBlankAuthCode() throws Exception {
        //given
        String expectedName = "김사자";
        String expectedEmail = "email@email.com";
        String expectedAuthCode = "";

        UserIdVerifyAuthCodeRequest request = UserIdVerifyAuthCodeRequest.builder()
                .name(expectedName)
                .email(expectedEmail)
                .authCode(expectedAuthCode)
                .build();

        String data = objectMapper.writeValueAsString(request);

        //when
        //then
        mockMvc.perform(
                        post("/api/users/valid-id-auth-code")
                                .with(csrf())
                                .contentType(APPLICATION_JSON)
                                .content(data)
                )
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("인증 코드 : 해당 입력은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}