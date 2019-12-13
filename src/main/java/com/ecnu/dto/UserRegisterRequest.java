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
    @Email
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    @Size(min = 1, max = 20)
    private String nickname;
    @Size(min = 6, max = 6)
    private String code;
}
