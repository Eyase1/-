package com.ttt.mapper;

import com.ttt.entity.user.User;

import java.math.BigInteger;

public interface UserLoginAndRegisterMapper {
    String getPasswordByUserName(String username);

    String getPasswordByEmail(String email);

    BigInteger getIdByUserName(String username);

    BigInteger getIdByEmail(String email);

    void insertUser(User user); // 插入用户信息
}
