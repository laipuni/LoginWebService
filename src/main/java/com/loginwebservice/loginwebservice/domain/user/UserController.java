package com.loginwebservice.loginwebservice.domain.user;

import com.loginwebservice.loginwebservice.domain.user.request.UserAddRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/join")
    public String joinForm(@ModelAttribute(name = "user") UserAddRequest request){
        return "user/userAddForm";
    }

    @PostMapping("/users/join")
    public String join(
            @Valid @ModelAttribute(name = "user") UserAddRequest request,
            BindingResult bindingResult,
            Model model
    ){
        if(bindingResult.hasErrors()){
            return "/user/userAddForm";
        }

        //중복된 유저가 존재해 에러가 발생한 경우
        try{
            userService.join(request);
        } catch (DataIntegrityViolationException exception){
            model.addAttribute("globalError","기입된 정보 중에 일치하는 유저가 존재합니다.");
            return "/user/userAddForm";
        }

        return "redirect:/users/join/success";
    }

    @GetMapping("/users/join/success")
    public String registerSuccess(){
        return "/user/registerSuccess";
    }
}
