package com.ecnu.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author onion
 * @date 2019/12/10 -11:00 下午
 */
@Data
public class Order {
    @Id
    private String id;
    private String gym;
    private String user;
    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean permitted;
}
