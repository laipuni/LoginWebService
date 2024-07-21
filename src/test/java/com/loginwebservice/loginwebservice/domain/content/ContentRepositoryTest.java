package com.loginwebservice.loginwebservice.domain.content;

import com.loginwebservice.loginwebservice.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class ContentRepositoryTest extends IntegrationTest {

    @Autowired
    ContentRepository contentRepository;

    @BeforeEach
    void tearUp(){
        contentRepository.deleteAllInBatch();
    }

    @DisplayName("기록된 컨텐츠들을 최신순으로 조회한다")
    @Test
    void findAllOrderByIdDesc(){
        //given
        Content content1 = Content.builder()
                .contents("내용1")
                .build();

        contentRepository.save(content1);

        Content content2 = Content.builder()
                .contents("내용2")
                .build();

        contentRepository.save(content2);

        //when
        List<Content> contents = contentRepository.findAllOrderByIdDesc();

        //then
        assertThat(contents).hasSize(2)
                .extracting("contents")
                .containsExactly(
                        "내용2", "내용1"
                );
    }

}