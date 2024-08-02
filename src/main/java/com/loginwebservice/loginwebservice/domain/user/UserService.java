package com.loginwebservice.loginwebservice.domain.user;

import com.loginwebservice.loginwebservice.domain.user.request.UserAddRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void join(UserAddRequest request){
        User user = User.of(
                request.getName(),
                request.getEmail(),
                null,
                Role.USER,
                request.getUserName(),
                request.getLoginId(),
                request.getPassword()
        );
        User result = userRepository.save(user);
    }

}
