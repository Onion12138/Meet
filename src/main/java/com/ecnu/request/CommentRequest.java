package com.ecnu.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author onion
 * @date 2019/12/11 -9:55 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequest {
    @NotBlank
    private String newsId;
    private String parentId;
    @NotBlank
    private String content;
}
