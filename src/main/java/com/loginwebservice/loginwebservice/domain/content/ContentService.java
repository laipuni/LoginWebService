package com.loginwebservice.loginwebservice.domain.content;

import com.loginwebservice.loginwebservice.ContentAddResponse;
import com.loginwebservice.loginwebservice.domain.content.request.ContentAddRequest;
import com.loginwebservice.loginwebservice.domain.content.response.ContentListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    @Transactional
    public ContentAddResponse save(final ContentAddRequest request) {
        Content content = contentRepository.save(Content.of(request.getContent()));
        return ContentAddResponse.of(content);
    }

    public ContentListResponse findAllOrderByIdDesc(){
        return ContentListResponse.of(contentRepository.findAllOrderByIdDesc());
    }

}
