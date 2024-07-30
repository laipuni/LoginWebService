package com.loginwebservice.loginwebservice.domain.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/users/join")
    public String joinForm(){
        return "login/userAddForm";
    }

}
