package com.ecnu.enums;

import lombok.Getter;

/**
 * @author onion
 * @date 2019/12/10 -11:08 下午
 */
@Getter
public enum ResultEnum {
    INVALID_TOKEN(1000, "token无效"),
    EMAIL_IN_USE(1001, "邮箱已经被注册了"),
    CODE_NOT_EXIST(1002, "验证码不存在"),
    WRONG_CODE(1003, "验证码错误"),
    USER_NOT_EXIST(1004, "用户不存在"),
    WRONG_PASSWORD(1005, "密码错误"),
    FILE_UPLOAD_ERROR(1006, "文件上传出错"),
    ACCOUNT_DISABLED(1007, "账户被禁用"),
    MUST_LOGIN(1008, "请先登陆"),
    NO_AUTHORITY(1009, "没有权限")
    ;
    private int code;
    private String message;
    ResultEnum(int code, String message){
        this.code = code;
        this.message = message;
    }
}
