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
    @Email
    private String email;
    @NotEmpty
    private String password;
}
