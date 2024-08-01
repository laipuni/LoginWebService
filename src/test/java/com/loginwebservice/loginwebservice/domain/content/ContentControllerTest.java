package com.loginwebservice.loginwebservice.domain.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loginwebservice.loginwebservice.domain.content.response.ContentAddResponse;
import com.loginwebservice.loginwebservice.domain.content.request.ContentAddRequest;
import com.loginwebservice.loginwebservice.domain.content.request.ContentAddServiceRequest;
import com.loginwebservice.loginwebservice.domain.content.response.ContentListResponse;
import com.loginwebservice.loginwebservice.domain.content.response.ContentViewResponse;
import com.loginwebservice.loginwebservice.security.resolver.dto.LoginUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ContentController.class)
class ContentControllerTest {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ContentService contentService;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @DisplayName("콘텐츠들 조회 Api")
    @WithMockUser()
    @Test
    void getContents() throws Exception {
        //given
        List<Content> contentList = List.of(
                Content.builder()
                        .contents("내용1")
                        .build()
        );

        List<ContentViewResponse> contentViewResponseList = contentList.stream()
                .map(ContentViewResponse::of)
                .toList();

        ContentListResponse response = ContentListResponse.builder()
                .size(contentViewResponseList.size())
                .contents(contentViewResponseList)
                .build();

        Mockito.when(contentService.findAllOrderByIdDesc()).thenReturn(response);
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/content"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.size").exists());

    }

    @DisplayName("내용을 받아 컨텐츠를 등록하고 결과를 반환한다.")
    @WithMockUser()
    @Test
    void homeInput() throws Exception {
        //given
        ContentAddRequest request = ContentAddRequest.builder()
                .content("안녕하세요")
                .build();

        LoginUser user = LoginUser.builder()
                .email("email@gmail.com")
                .name("라이푸니")
                .build();

        String data = objectMapper.writeValueAsString(request);
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();

        ContentAddResponse response = ContentAddResponse.builder()
                .contentId(0L)
                .contents("안녕하세요")
                .createDate(LocalDate.of(2024,7,15))
                .build();

        Mockito.when(contentService.save(Mockito.any(ContentAddServiceRequest.class)))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/content")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(data)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.code").value("201"))
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName("컨텐츠를 등록할 때, 내용이 없을 경우 에러가 발생한다.")
    @WithMockUser()
    @Test
    void homeInputWithBlankContents() throws Exception {
        //given
        ContentAddRequest request = ContentAddRequest.builder()
                .content(" ")
                .build();

        String data = objectMapper.writeValueAsString(request);

        ContentAddResponse response = ContentAddResponse.builder()
                .contentId(0L)
                .contents("안녕하세요")
                .createDate(LocalDate.of(2024,7,15))
                .build();

        Mockito.when(contentService.save(Mockito.any(ContentAddServiceRequest.class)))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/content")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(data)

                )
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("내용을 입력해주세요."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}