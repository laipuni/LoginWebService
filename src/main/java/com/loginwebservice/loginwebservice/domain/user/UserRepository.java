package com.loginwebservice.loginwebservice.domain.user;

import com.loginwebservice.loginwebservice.domain.user.response.UserIdVerifyAuthCodeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    public Optional<User> findUserByEmail(String email);

    public boolean existsUserByUserName (String userName);

    public boolean existsUserByLoginId (String loginId);

    public boolean existsUserByEmailAndName(String email, String name);

    public Optional<User> findUserByLoginId(String loginId);

    @Query("select new com.loginwebservice.loginwebservice.domain.user.response.UserIdVerifyAuthCodeResponse(u.loginId) " +
            "from User u " +
            "where u.name =:name and u.email=:email")
    public Optional<UserIdVerifyAuthCodeResponse> findByNameAndEmail(@Param("name") String name, @Param("email") String email);
}
