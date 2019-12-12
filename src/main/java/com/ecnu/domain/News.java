package com.ecnu.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author onion
 * @date 2019/12/10 -10:55 下午
 */
@Data
public class News implements Serializable {
    @Id
    private String id;
    private String publisher;
    private String nickname;
    private LocalDateTime publishTime;
    private String content;
}
