package com.loginwebservice.loginwebservice.domain.content;

import com.loginwebservice.loginwebservice.domain.content.response.ContentAddResponse;
import com.loginwebservice.loginwebservice.api.ApiResponse;
import com.loginwebservice.loginwebservice.domain.content.request.ContentAddRequest;
import com.loginwebservice.loginwebservice.domain.content.request.ContentAddServiceRequest;
import com.loginwebservice.loginwebservice.domain.content.response.ContentListResponse;
import com.loginwebservice.loginwebservice.security.annotation.UserInfo;
import com.loginwebservice.loginwebservice.security.resolver.dto.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ContentController {

    private final ContentService contentService;

    @GetMapping("/content")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ContentListResponse> getContents(){
        ContentListResponse contentList = contentService.findAllOrderByIdDesc();
        return ApiResponse.of(HttpStatus.OK,contentList);
    }

    @PostMapping("/content")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ContentAddResponse> homeInput(@Valid @RequestBody ContentAddRequest content, @UserInfo LoginUser user){
        ContentAddResponse response = contentService.save(
                ContentAddServiceRequest.of(content.getContent(),user.getEmail())
        );
        return ApiResponse.of(HttpStatus.CREATED,response);
    }
}
