package com.ttt.controller;

import com.ttt.entity.user.User;
import com.ttt.service.UserLoginAndRegisterService;
import com.ttt.util.result.Result;
import com.ttt.util.result.ResultCodeEnum;
import com.ttt.util.user.VerificationCodeCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserLoginAndRegisterController {
    @Autowired
    private UserLoginAndRegisterService userLoginAndRegisterService;
    
    @PostMapping("/isAdmin")
    public boolean isAdmin(@RequestBody String token){
        return userLoginAndRegisterService.isAdmin(token);
    }


    @PostMapping("/login")
    public Result login(@RequestBody User user){
        if(user.getUsername()==null||user.getUsername().trim().equals("")){
            throw new RuntimeException("用户名为空");
        }
        if(user.getPassword()==null||user.getPassword().trim().equals("")){
            throw  new RuntimeException("当前密码为空");
        }
        return userLoginAndRegisterService.login(user);
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        return userLoginAndRegisterService.register(user, user.getCode());
    }

    @GetMapping("/sendEmailCode")
    public Result<String> sendEmailCode(@RequestParam String email) {
        try {
            userLoginAndRegisterService.sendEmailCode(email);
            return Result.ok("验证码已发送");
        } catch (RuntimeException e) {
            return Result.build(null, ResultCodeEnum.PASSWORD_ERROR.getCode(), "验证码发送失败", false);
        }
    }

    @PostMapping("/verfiy/{code}")
    public Result verfiy(@PathVariable String code,@RequestParam String email) {

        if (!VerificationCodeCache.validateAndNotRemote(email, code)) {
            return Result.build(null, ResultCodeEnum.PASSWORD_ERROR.getCode(), "验证码错误或已过期", false);
        }
        return Result.ok(null);
    }
}
