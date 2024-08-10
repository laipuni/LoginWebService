package com.loginwebservice.loginwebservice.domain.user;

import com.loginwebservice.loginwebservice.api.ApiResponse;
import com.loginwebservice.loginwebservice.domain.user.req.UserIdVerifyAuthCodeRequest;
import com.loginwebservice.loginwebservice.domain.user.request.UserIdSendAuthCodeRequest;
import com.loginwebservice.loginwebservice.domain.user.response.UserIdVerifyAuthCodeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserApiController {

    private final UserService userService;
    private final UserHelpService userHelpService;

    @GetMapping("/users/join/check-login-id")
    public ApiResponse<Boolean> isExistSameLoginIdUser(@RequestParam("loginId") String loginId){
        boolean result = userService.isExistSameLoginIdUSer(loginId);
        return ApiResponse.of(HttpStatus.OK,result);
    }

    @GetMapping("/users/join/check-user-name")
    public ApiResponse<Boolean> isExistSameUserName(@RequestParam("userName") String userName){
        boolean result = userService.isExistSameUserName(userName);
        return ApiResponse.of(HttpStatus.OK,result);
    }

    @PostMapping("/users/send-id-auth-code")
    public ApiResponse<Object> sendAuthCode(@Valid @RequestBody UserIdSendAuthCodeRequest request){
        userHelpService.helpUserId(request.getName(),request.getEmail());
        return ApiResponse.of(HttpStatus.OK,null);
    }

    @PostMapping("/users/valid-id-auth-code")
    public ApiResponse<UserIdVerifyAuthCodeResponse> verifyAuthCode(@Valid @RequestBody UserIdVerifyAuthCodeRequest request){
        UserIdVerifyAuthCodeResponse response = userHelpService.verifyHelpUserIdAuthCode(request.getAuthCode(), request.getName(), request.getEmail());
        return ApiResponse.of(HttpStatus.OK,response);
    }

}
