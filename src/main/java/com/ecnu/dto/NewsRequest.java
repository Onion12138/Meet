package com.ecnu.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author onion
 * @date 2019/12/11 -11:24 下午
 */
@Data
public class NewsRequest {
    private String newsId;
    @NotEmpty
    private String content;
}
