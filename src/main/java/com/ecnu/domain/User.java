package com.ecnu.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author onion
 * @date 2019/12/10 -10:50 下午
 */
@Data
@Table(name = "user")
public class User implements Serializable {
    @Id
    private String email;
    private String password;
    private String nickname;
    @Column(name = "profile_url")
    private String profileUrl;
    @Column(name = "register_time")
    private LocalDateTime registerTime;
    private Integer credit; //信誉积分
    private Boolean disabled;
    private Boolean admin;
}
