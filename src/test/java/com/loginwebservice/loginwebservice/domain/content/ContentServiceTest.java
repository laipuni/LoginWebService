package com.loginwebservice.loginwebservice.domain.content;

import com.loginwebservice.loginwebservice.ContentAddResponse;
import com.loginwebservice.loginwebservice.domain.content.request.ContentAddRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ContentServiceTest {

    @Autowired
    ContentService contentService;

    @Autowired
    ContentRepository contentRepository;


    @DisplayName("기록할 내용을 받아서 기록 한다")
    @Test
    void save(){
        //given
        String contents = "안녕하세요";
        ContentAddRequest request = ContentAddRequest.builder()
                .content(contents)
                .build();
        //when
        ContentAddResponse response = contentService.save(request);
        List<Content> contentList = contentRepository.findAll();
        Content content = contentList.get(0);

        //then
        assertThat(contentList).hasSize(1)
                .extracting("contents")
                .containsExactlyInAnyOrder(
                        contents
                );
        assertThat(response)
                .extracting("id","date","contents")
                .containsExactly(
                        content.getId(),content.getCreateDate(),content.getContents()
                );
    }

}