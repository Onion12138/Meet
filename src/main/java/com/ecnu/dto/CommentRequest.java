package com.ecnu.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author onion
 * @date 2019/12/11 -9:55 下午
 */
@Data
public class CommentRequest {
    @NotBlank
    private String newsId;
    private String parentId;
    @NotBlank
    private String content;
}
