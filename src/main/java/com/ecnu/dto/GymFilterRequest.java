package com.ecnu.dto;

import lombok.Data;

/**
 * @author onion
 * @date 2019/12/12 -2:14 下午
 */
@Data
public class GymFilterRequest {
    private String name;
    private String address;
    private Boolean highToLow;
    private Boolean openOnly;
}
