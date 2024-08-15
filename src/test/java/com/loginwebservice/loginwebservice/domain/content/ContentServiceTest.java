package com.loginwebservice.loginwebservice.domain.content;

import com.loginwebservice.loginwebservice.IntegrationTest;
import com.loginwebservice.loginwebservice.domain.content.request.ContentAddServiceRequest;
import com.loginwebservice.loginwebservice.domain.user.Role;
import com.loginwebservice.loginwebservice.domain.user.User;
import com.loginwebservice.loginwebservice.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ContentServiceTest extends IntegrationTest {

    @Autowired
    ContentService contentService;

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void tearUp(){
        contentRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("기록할 내용을 받아서 기록 한다")
    @Transactional
    @Test
    void save(){
        //given
        String expectedEmail = "email@gmail.com";
        String expectedName = "김사자";
        String expectedUserName = "laipuni";
        User user = User.builder()
                .name(expectedName)
                .userName(expectedUserName)
                .email(expectedEmail)
                .role(Role.GUEST)
                .build();
        userRepository.save(user);
        String contents = "안녕하세요";
        ContentAddServiceRequest request = ContentAddServiceRequest.builder()
                .content(contents)
                .email(expectedEmail)
                .build();
        //when
        contentService.save(request);
        List<Content> contentList = contentRepository.findAll();
        //then
        assertThat(contentList).hasSize(1)
                .extracting("contents","userName")
                .containsExactlyInAnyOrder(
                        tuple(contents,expectedUserName)
                );
        assertThat(contentList.get(0).getUser())
                .extracting("userName","email")
                .containsExactly(expectedUserName,expectedEmail);
    }

    @DisplayName("컨텐츠를 등록할 때, 등록되지 않은 유저일 경우 에러가 발생한다.")
    @Test
    void saveWithNotExistUser(){
        //given
        String contents = "안녕하세요";
        ContentAddServiceRequest request = ContentAddServiceRequest.builder()
                .content(contents)
                .email("email@gmail.com")
                .build();
        //when
        //then
        assertThatThrownBy(() -> contentService.save(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("해당 유저는 등록되지 않았습니다.");
    }

}