package com.ecnu.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author onion
 * @date 2019/12/10 -10:57 下午
 */
@Data
public class Gym {
    @Id
    private String id;
    private String name;
    private String description;
    private String address;
    private Double rent;
}
