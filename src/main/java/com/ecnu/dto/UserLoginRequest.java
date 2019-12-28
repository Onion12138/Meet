package com.ecnu.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * @author onion
 * @date 2019/12/11 -9:15 上午
 */
@Data
public class UserLoginRequest {
    @Email(message = "请输入合法的邮箱")
    private String email;
    @NotEmpty(message = "密码不能为空")
    private String password;
}
