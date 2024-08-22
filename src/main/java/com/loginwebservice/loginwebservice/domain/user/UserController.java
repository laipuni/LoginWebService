package com.loginwebservice.loginwebservice.domain.user;

import com.loginwebservice.loginwebservice.domain.user.request.UserAddRequest;
import com.loginwebservice.loginwebservice.domain.user.request.UserAddServiceRequest;
import com.loginwebservice.loginwebservice.redis.RedisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRegisterService userRegisterService;
    private final RedisService redisService;

    @GetMapping("/users/join")
    public String joinForm(@ModelAttribute(name = "user") UserAddRequest request){
        return "user/userAddForm";
    }

    @PostMapping("/users/join")
    public String join(
            @Valid @ModelAttribute(name = "user") UserAddRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes attributes
    ){
        if(bindingResult.hasErrors()){
            return "user/userAddForm";
        }

        try{
            userRegisterService.register(UserAddServiceRequest.of(request));
        } catch (DataIntegrityViolationException exception){
            //중복된 유저가 존재할 경우
            model.addAttribute("globalError","기입된 정보 중에 일치하는 유저가 존재합니다.");
            return "user/userAddForm";
        }

        attributes.addFlashAttribute("email",request.getEmail());
        return "redirect:/users/join/register-auth";
    }

    @GetMapping("/users/join/{loginId}/verify-register")
    public String verifyRegister(
            @PathVariable("loginId")String loginId
    ){
        userRegisterService.verifyRegister(loginId);
        return "redirect:/users/join/success";
    }

    @GetMapping("/users/join/register-auth")
    public String alertRegisterAuth(
            Model model
    ){
        return "user/alertRegisterAuth";
    }

    @GetMapping("/users/join/success")
    public String registerSuccess(){
        return "user/registerSuccess";
    }

    @GetMapping("/users/help-id")
    public String findIdForm(){
        return "user/help/userIdHelpForm";
    }

    @GetMapping("/users/help-password")
    public String findPasswordForm(
            @RequestParam(defaultValue = "viewLoginIdInput",name = "menu") String menu,
            @RequestParam(required = false,name = "token_help") String token,
            Model model
    ){
        if(token != null){
            return viewNextFormBy(token,menu,model);
        }

        return "user/help/userLoginIdInputForm";
    }

    private String viewNextFormBy(final String token, final String menu, final Model model) {
        //유효하지 않을 경우 해당 세션이 만료됐다는 메세지와 함께 login화면으로 가도록
        if(!redisService.existData(token)){
            return "redirect:/users/help-password";
        }

        model.addAttribute("token_help",token);

        if(menu.equals("viewPasswordAuth")){
            //인증 화면
            return "user/help/userPasswordAuthForm";
        } else if(menu.equals("viewPasswordInput")){
            //비밀번호 재설정
            return createViewUserPasswordInputForm(token, model);
        } else{
            return "user/help/userLoginIdInputForm";
        }
    }

    private String createViewUserPasswordInputForm(final String token, final Model model) {
        String loginId = redisService.getData(token);
        model.addAttribute("loginId",loginId);
        return "user/help/userPasswordInputForm";
    }


}
