package com.ecnu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author onion
 * @date 2019/12/10 -10:50 下午
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "user")
public class User implements Serializable {
    @Id
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String nickname;
    @Column
    private String profile;
    @Column(name = "register_time")
    private LocalDateTime registerTime;
    @Column
    private Integer credit; //信誉积分
    @Column
    private Boolean disabled;
    @Column
    private Boolean admin = false;
//    @OneToMany(targetEntity = Order.class)
//    @JoinColumn(name = "user_email", referencedColumnName = "email")
    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private Set<Order> orderSet;

}
