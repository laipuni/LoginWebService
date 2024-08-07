package com.loginwebservice.loginwebservice.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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

}