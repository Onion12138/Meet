package com.ecnu.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @date 2019/12/11 -11:24 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsRequest {
    private String newsId;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
}
