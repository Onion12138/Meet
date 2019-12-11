package com.ecnu.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * @author onion
 * @date 2019/12/10 -10:50 下午
 */
@Data
public class User {
    @Id
    private String id;
    private String email;
    private String password;
    private String nickname;
    private String profileUrl;
    private LocalDateTime registerTime;
    private Integer credit; //信誉积分
    private boolean disabled;
    private boolean admin;
}
