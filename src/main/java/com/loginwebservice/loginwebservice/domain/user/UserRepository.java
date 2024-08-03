package com.loginwebservice.loginwebservice.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    public Optional<User> findUserByEmail(String email);

    public boolean existsUserByUserName (String userName);

    public boolean existsUserByLoginId (String loginId);
}
