package com.ecnu.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author onion
 * @date 2019/12/10 -11:00 下午
 */
@Data
public class Order implements Serializable {
    @Id
    private String id;
    private String gym;
    private String email;
    private LocalDate date;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean cancel; //是否被取消
    private boolean valid; //类似于一个签到系统，创建一个定时任务，更新用户信誉分数
    private Integer score;
    private String comment; //评价
}
