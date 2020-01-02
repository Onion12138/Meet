package com.ecnu.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * @date 2019/12/11 -9:15 上午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginRequest {
    @Email(message = "请输入合法的邮箱")
    private String email;
    @NotEmpty(message = "密码不能为空")
    private String password;

}
