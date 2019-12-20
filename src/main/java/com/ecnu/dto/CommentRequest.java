package com.ecnu.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author onion
 * @date 2019/12/11 -9:55 下午
 */
@Data
public class CommentRequest {
    @NotEmpty
    private String newsId;
    private String parentId;
    @NotEmpty
    private String content;
}
