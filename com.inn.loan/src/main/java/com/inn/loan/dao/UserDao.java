package com.inn.loan.dao;

import com.inn.loan.POJO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserDao extends JpaRepository<User,String> {
    User findbyEmailId(@Param("email") String email);
}
