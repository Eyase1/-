package com.ttt.service.impl;

import com.ttt.entity.user.User;
import com.ttt.mapper.UserInfoMapper;
import com.ttt.service.UserInfoService;
import com.ttt.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public User getUserById(BigInteger userId) {
        return userInfoMapper.selectUserById(userId);
    }

    @Override
    public User updateUser(BigInteger userId, User user,String token) {
        // 先查询用户是否存在
        User existingUser = userInfoMapper.selectUserById(userId);
        if (existingUser != null) {
            // 更新用户信息
            user.setId(userId);
            userInfoMapper.updateUser(user);
            return user;
        }
        return null;
    }


}
