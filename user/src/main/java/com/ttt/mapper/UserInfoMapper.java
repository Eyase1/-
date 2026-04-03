package com.ttt.mapper;

import com.ttt.entity.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;

@Mapper
public interface UserInfoMapper {
    /**
     * 根据 userId 查询用户信息
     * @param userId 用户ID
     * @return User 实体类
     */
    User selectUserById(@Param("userId") BigInteger userId);

    /**
     * 更新用户信息
     * @param user 用户信息
     */
    void updateUser(User user);
}
