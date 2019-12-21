package com.ecnu.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author onion
 * @date 2019/12/21 -9:23 上午
 */
@Data
public class OrderCommentRequest {
    @NotEmpty
    private String orderId;
    @NotNull
    private Integer score;
    @NotEmpty
    private String comment;
}
