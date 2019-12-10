package com.ecnu.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author onion
 * @date 2019/12/10 -11:00 下午
 */
@Data
public class Comment {
    @Id
    private String id;
    private String news;
    private String parentId;
    private String publisher;
    private String content;
}
