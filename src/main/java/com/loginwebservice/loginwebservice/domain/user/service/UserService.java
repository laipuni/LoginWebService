package com.loginwebservice.loginwebservice.domain.user.service;

import com.loginwebservice.loginwebservice.domain.PasswordEncodeService;
import com.loginwebservice.loginwebservice.domain.user.Role;
import com.loginwebservice.loginwebservice.domain.user.User;
import com.loginwebservice.loginwebservice.domain.user.UserRepository;
import com.loginwebservice.loginwebservice.domain.user.request.UserAddServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncodeService encodeService;

    @Transactional
    public void join(UserAddServiceRequest request){
        String encodedPassword = encodeService.encodePassword(request.getPassword());
        User user = User.of(
                request.getName(),
                request.getEmail(),
                null,
                Role.USER,
                request.getUserName(),
                request.getLoginId(),
                encodedPassword
        );
        userRepository.save(user);
    }

    public boolean isExistSameUserName(final String userName){
        return userRepository.existsUserByUserName(userName);
    }

    public boolean isExistSameLoginIdUSer(final String loginId){
        return userRepository.existsUserByLoginId(loginId);
    }

}
