package com.loginwebservice.loginwebservice.domain.user.controller;

import com.loginwebservice.loginwebservice.api.ApiResponse;
import com.loginwebservice.loginwebservice.domain.user.request.*;
import com.loginwebservice.loginwebservice.domain.user.response.LoginIdSearchResponse;
import com.loginwebservice.loginwebservice.domain.user.response.LoginIdValidationResponse;
import com.loginwebservice.loginwebservice.domain.user.response.PasswordAuthCodeValidResponse;
import com.loginwebservice.loginwebservice.domain.user.service.UserHelpService;
import com.loginwebservice.loginwebservice.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;
    private final UserHelpService userHelpService;

    @GetMapping("/join/check-login-id")
    public ApiResponse<Boolean> isExistSameLoginIdUser(@RequestParam("loginId") String loginId){
        boolean result = userService.isExistSameLoginIdUSer(loginId);
        return ApiResponse.of(HttpStatus.OK,result);
    }

    @GetMapping("/join/check-user-name")
    public ApiResponse<Boolean> isExistSameUserName(@RequestParam("userName") String userName){
        boolean result = userService.isExistSameUserName(userName);
        return ApiResponse.of(HttpStatus.OK,result);
    }

    @PostMapping("/valid-id-auth-code")
    public ApiResponse<LoginIdValidationResponse> validLoginIdAuthCode(@Valid @RequestBody LoginIdAuthCodeValidRequest request){
        LoginIdValidationResponse response = userHelpService.validHelpLoginIdAuthCode(request.getAuthCode(), request.getName(), request.getEmail());
        return ApiResponse.of(HttpStatus.OK,response);
    }

    @PostMapping("/send-id-auth-code")
    public ApiResponse<Object> sendLoginIdAuthCode(@Valid @RequestBody LoginIdAuthCodeSendRequest request){
        userHelpService.sendHelpLoginIdAuthMail(request.getName(),request.getEmail());
        return ApiResponse.of(HttpStatus.OK,null);
    }

    @GetMapping("/search-loginId")
    public ApiResponse<LoginIdSearchResponse> isExistLoginId(@RequestParam("loginId") String loginId){
        String helpToken = UUID.randomUUID().toString();
        return ApiResponse.of(HttpStatus.OK, userHelpService.searchLoginId(loginId,helpToken));
    }

    @PostMapping("/send-password-auth-code")
    public ApiResponse<Object> sendPasswordAuthCode(@Valid @RequestBody PasswordAuthCodeSendRequest request){
        userHelpService.sendHelpPasswordAuthMail(request.getName(), request.getEmail());
        return ApiResponse.of(HttpStatus.OK,null);
    }
    
    @PostMapping("/valid-password-auth-code")
    public ApiResponse<PasswordAuthCodeValidResponse> validPasswordAuthCode(@Valid @RequestBody PasswordAuthCodeValidRequest request){
        PasswordAuthCodeValidResponse response = userHelpService.validPasswordAuthCode(
                request.getAuthCode(), request.getName(), request.getEmail()
        );
        return ApiResponse.of(HttpStatus.OK, response);
    }

    @PostMapping("/reset-password")
    public ApiResponse<Object> resetPassword(@Valid @RequestBody PasswordResetRequest request){
        userHelpService.resetPassword(
                request.getToken(),request.getPassword()
        );
        return ApiResponse.of(HttpStatus.OK,null);
    }
}
