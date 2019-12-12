package com.ecnu.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author onion
 * @date 2019/12/11 -9:32 下午
 */
@Data
public class GymComment implements Serializable {
    @Id
    private String id;
    private String userId;
    private String nickname;
    private String profileUrl;
    private String name;
    private String description;
    private String address;
    private LocalDate date;
    private Double score;
    private String comment;
}
