package com.ecnu.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;

/**
 * @author onion
 * @date 2019/12/11 -11:18 下午
 */
@Data
public class OrderRequest {
    @NotEmpty
    private String gymId;
    private String userEmail;
    @NotEmpty
    private String date;
    @Range(min = 16, max = 39)
    private Integer startTime;
    @Range(min = 17, max = 40)
    private Integer endTime;
}
