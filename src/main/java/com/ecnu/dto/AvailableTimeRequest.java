package com.ecnu.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author onion
 * @date 2019/12/21 -9:37 上午
 */
@Data
public class AvailableTimeRequest {
    @NotEmpty
    private String date;
    @NotEmpty
    private String gymId;
}
