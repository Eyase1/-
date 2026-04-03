package com.ttt.controller;

import com.ttt.entity.user.User;
import com.ttt.service.UserInfoService;
import com.ttt.service.impl.UserInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 根据 userId 查询用户信息
     * @param userId 用户ID
     * @return User 实体类
     */
    @GetMapping("/{userId}")
    public User getUserInfo(@PathVariable("userId") BigInteger userId) {
        return userInfoService.getUserById(userId);
    }

    /**
     * 修改用户信息
     * @param userId 用户ID
     * @param user 更新后的用户信息
     * @return 更新后的 User 实体类
     */
    @PutMapping
    public User updateUserInfo(@RequestBody User user,@RequestHeader("token")String token) {
        return userInfoService.updateUser(user.getId(), user,token);
    }
}
