package com.loginwebservice.loginwebservice.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(
            @RequestParam(required = false, name = "errorMessage")String errorMessage,
            Model model){

        if(errorMessage != null){
            model.addAttribute("errorMessage",errorMessage);
        }

        return "login/login";
    }
}
