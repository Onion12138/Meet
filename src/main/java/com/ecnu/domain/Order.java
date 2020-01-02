package com.ecnu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @date 2019/12/10 -11:00 下午
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "orders")
public class Order implements Serializable {
    @Id
    @Column(name = "order_id")
    private String orderId;
    @Column(name = "order_gym_id")
    private String gymId;
    @Column(name = "user_email")
    private String userEmail;
    @Column(name = "order_date")
    private LocalDate orderDate;
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;
    @Column(name = "cancel")
    private Boolean cancel; //是否被取消
    @Column
    private Boolean valid; //类似于一个签到系统，创建一个定时任务，更新用户信誉分数
    @Column
    private Integer score;
    @Column
    private String comment; //评价
    @ManyToOne(targetEntity = User.class)
    @JsonIgnoreProperties("orderSet")
    @JoinColumn(name = "user_email", referencedColumnName = "email", insertable = false, updatable = false)
    private User user;
    @ManyToOne(targetEntity = Gym.class)
    @JsonIgnoreProperties("orderSet")
    @JoinColumn(name = "order_gym_id", referencedColumnName = "gym_id", insertable = false, updatable = false)
    private Gym gym;
}
