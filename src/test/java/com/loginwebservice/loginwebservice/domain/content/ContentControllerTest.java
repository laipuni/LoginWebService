package com.loginwebservice.loginwebservice.domain.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loginwebservice.loginwebservice.ContentAddResponse;
import com.loginwebservice.loginwebservice.domain.content.request.ContentAddRequest;
import com.loginwebservice.loginwebservice.domain.content.response.ContentListResponse;
import com.loginwebservice.loginwebservice.domain.content.response.ContentViewResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ContentController.class)
class ContentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ContentService contentService;

    @DisplayName("콘텐츠들 조회 Api")
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

    @DisplayName("내용 등록 Api")
    @Test
    void homeInput() throws Exception {
        //given
        ContentAddRequest request = ContentAddRequest.builder()
                .content("안녕하세요")
                .build();

        String data = objectMapper.writeValueAsString(request);

        ContentAddResponse response = ContentAddResponse.builder()
                .contentId(0L)
                .contents("안녕하세요")
                .createDate(LocalDate.of(2024,7,15))
                .build();

        Mockito.when(contentService.save(Mockito.any(ContentAddRequest.class)))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/content")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(data)

                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.code").value("201"))
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName("빈 내용이 요청으로 올 경우 에러가 발생한다.")
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

        Mockito.when(contentService.save(Mockito.any(ContentAddRequest.class)))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/content")
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