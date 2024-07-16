package com.loginwebservice.loginwebservice.domain.content;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content,Long> {

    @Query(value = "select c from Content c order by c.id desc")
    List<Content> findAllOrderByIdDesc();
}
