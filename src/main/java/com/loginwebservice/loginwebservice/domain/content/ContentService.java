package com.loginwebservice.loginwebservice.domain.content;

import com.loginwebservice.loginwebservice.domain.content.response.ContentAddResponse;
import com.loginwebservice.loginwebservice.domain.content.request.ContentAddServiceRequest;
import com.loginwebservice.loginwebservice.domain.content.response.ContentListResponse;
import com.loginwebservice.loginwebservice.domain.user.User;
import com.loginwebservice.loginwebservice.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContentService {

    private final UserRepository userRepository;
    private final ContentRepository contentRepository;

    @Transactional
    public ContentAddResponse save(final ContentAddServiceRequest request) {
        User user = userRepository.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));
        Content content = contentRepository.save(Content.of(user,request.getContent()));
        return ContentAddResponse.of(content);
    }

    public ContentListResponse findAllOrderByIdDesc(){
        return ContentListResponse.of(contentRepository.findAllOrderByIdDesc());
    }

}
