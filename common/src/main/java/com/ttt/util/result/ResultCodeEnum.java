package com.ttt.util.result;

/**
 * 统一返回结果状态信息类
 *
 */
public enum ResultCodeEnum {

    SUCCESS(200,"success",true),
    USERNAME_ERROR(501,"usernameError",false),
    PASSWORD_ERROR(503,"passwordError",false),
    NOTLOGIN(504,"notLogin",false),
    USERNAME_USED(505,"userNameUsed",false),
    VERIFICATION_CODE_ERROR(506, "verificationCodeError", false), // 新增验证码错误
    INVALID_EMAIL(507, "invalidEmail", false), // 新增无效邮箱错误
    REGISTRATION_FAILED(508, "registrationFailed", false); // 新增注册失败错误
    private Integer code;
    private String message;
    private boolean success;
    private ResultCodeEnum(Integer code, String message,boolean success) {
        this.code = code;
        this.success=success;
        this.message = message;
    }
    public Integer getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public boolean isSuccess() {return success;}
}
