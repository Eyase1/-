package com.ttt.service.impl;

import com.ttt.entity.user.User;
import com.ttt.mapper.UserLoginAndRegisterMapper;
import com.ttt.resp.user.UserLoginAndRegisterResp;
import com.ttt.service.UserLoginAndRegisterService;
import com.ttt.util.UserUtil;
import com.ttt.util.result.Result;
import com.ttt.util.result.ResultCodeEnum;
import com.ttt.util.user.EmailUtils;
import com.ttt.util.user.JwtHelper;
import com.ttt.util.user.MD5Util;
import com.ttt.util.user.VerificationCodeCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class UserLoginAndRegisterServiceImpl implements UserLoginAndRegisterService {
    @Autowired
    private UserLoginAndRegisterMapper userLoginAndRegisterMapper;
    @Autowired
    private UserUtil util;
    @Override
    public Result login(User user) {
        UserLoginAndRegisterResp resp = new UserLoginAndRegisterResp();
        BigInteger id=userLoginAndRegisterMapper.getIdByUserName(user.getUsername());
        if(id==null){
            id=userLoginAndRegisterMapper.getIdByEmail(user.getUsername());
        }
        if(id==null){
            return Result.build(null,400,"当前用户不存在");
        }

        String token=JwtHelper.createToken(Long.valueOf(String.valueOf(id)));
        resp.setToken(token);
        resp.setId(id);
        user.setPassword(MD5Util.encrypt(user.getPassword()));
        String password=userLoginAndRegisterMapper.getPasswordByUserName(user.getUsername());
        if(password==null||password.trim().equals("")||!password.equals(user.getPassword())){
            password=userLoginAndRegisterMapper.getPasswordByEmail(user.getUsername());
        }else{
            return Result.ok(resp);
        }
        if(password==null||password.trim().equals("")||!password.equals(user.getPassword())){
            return Result.build(null,400,"用户名或密码错误");
        }

        System.out.println(JwtHelper.getUserId(token));
        return Result.ok(resp);
    }

    @Override
    public Result register(User user, String code) {
        // 1. 验证邮箱格式
        if (!isValidEmail(user.getEmail())) {
            return Result.build(null, ResultCodeEnum.PASSWORD_ERROR.getCode(), "邮箱格式不正确", false);
        }

        // 2. 检查用户名是否已存在
        if (userLoginAndRegisterMapper.getIdByUserName(user.getUsername()) != null) {
            return Result.build(null, ResultCodeEnum.USERNAME_USED.getCode(), "用户名已存在", false);
        }

        // 3. 检查邮箱是否已存在
        if (userLoginAndRegisterMapper.getIdByEmail(user.getEmail()) != null) {
            return Result.build(null, ResultCodeEnum.USERNAME_USED.getCode(), "邮箱已注册", false);
        }

        /*
        if (!isPasswordStrong(user.getPassword())) {
            return Result.build(null, ResultCodeEnum.PASSWORD_ERROR.getCode(), "密码强度不足", false);
        }

        private boolean isPasswordStrong(String password) {
            // 至少8个字符，包含大小写字母和数字
            return password != null && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
        }
         */


        // 4. 验证验证码（实际项目中应替换为缓存验证）
        if (!VerificationCodeCache.validate(user.getEmail(), code)) {
            return Result.build(null, ResultCodeEnum.PASSWORD_ERROR.getCode(), "验证码错误或已过期", false);
        }

        // 5. 使用MD5加密密码
        user.setPassword(MD5Util.encrypt(user.getPassword()));
        user.setRole("student"); // 设置默认角色

        // 6. 保存用户信息
        try {
            userLoginAndRegisterMapper.insertUser(user);
            return Result.build(null, ResultCodeEnum.SUCCESS.getCode(), "注册成功", true);
        } catch (Exception e) {
            return Result.build(null, ResultCodeEnum.PASSWORD_ERROR.getCode(), "注册失败：" + e.getMessage(), false);
        }
    }

    @Override
    public void sendEmailCode(String email) {
        // 1. 验证邮箱格式
        if (!isValidEmail(email)) {
            throw new RuntimeException("邮箱格式不正确");
        }

        // 2. 检查邮箱是否已注册
        if (userLoginAndRegisterMapper.getIdByEmail(email) != null) {
            throw new RuntimeException("该邮箱已注册");
        }

        // 3. 生成并发送验证码
        String code = EmailUtils.generateCode();
        EmailUtils.sendEmailCode(email, code);

        // 4. 存储验证码到缓存
        VerificationCodeCache.put(email, code);
        System.out.println("Generated code for " + email + ": " + code);
    }

    @Override
    public boolean isAdmin(String token) {
        return util.isAdmin(token);
    }

    // 邮箱格式验证方法
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

}
