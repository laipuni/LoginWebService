package com.loginwebservice.loginwebservice.domain.user;

import com.loginwebservice.loginwebservice.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserApiController {

    private final UserService userService;

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


}
