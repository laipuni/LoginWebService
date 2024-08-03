package com.loginwebservice.loginwebservice.domain.user;

import com.loginwebservice.loginwebservice.domain.PasswordEncodeService;
import com.loginwebservice.loginwebservice.domain.user.request.UserAddRequest;
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
    public void join(UserAddRequest request){
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
        User result = userRepository.save(user);
    }

    public boolean isExistSameUserName(final String userName){
        return userRepository.existsUserByUserName(userName);
    }

    public boolean isExistSameLoginIdUSer(final String loginId){
        return userRepository.existsUserByLoginId(loginId);
    }

}
