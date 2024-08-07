package com.loginwebservice.loginwebservice.security.formLogin.domain;

import com.loginwebservice.loginwebservice.domain.user.Role;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class SecurityUser extends User {

    private Long userSeq;
    private String email;
    private String picture;

    public SecurityUser(com.loginwebservice.loginwebservice.domain.user.User user){
        super(user.getLoginId(), user.getPassword(), createAuthority(List.of(user.getRole())));
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.userSeq = user.getId();
    }

    private static List<SimpleGrantedAuthority> createAuthority(List<Role> roleList){
        return roleList.stream()
                .map(role -> new SimpleGrantedAuthority(role.getKey()))
                .toList();
    }

}
