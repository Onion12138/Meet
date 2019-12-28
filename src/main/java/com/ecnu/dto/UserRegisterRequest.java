package com.ecnu.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author onion
 * @date 2019/12/10 -11:03 下午
 */
@Data
public class UserRegisterRequest {
    @Email(message = "请输入合法邮箱")
    private String email;
    @NotEmpty(message = "密码不能为空")
    private String password;
    @NotEmpty(message = "字符在1-20个之间")
    @Size(min = 1, max = 20)
    private String nickname;
    @Size(min = 6, max = 6)
    private String code;
}
