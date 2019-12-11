package com.ecnu.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;

/**
 * @author onion
 * @date 2019/12/10 -10:57 下午
 */
@Data
public class Gym {
    @Id
    private String id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotEmpty
    private String address;
    @DecimalMin(value = "0")
    private Double rent;
    private boolean open;
}
