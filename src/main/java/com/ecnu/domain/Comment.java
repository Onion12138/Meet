package com.ecnu.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

/**
 * @author onion
 * @date 2019/12/10 -11:00 下午
 */
@Data
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String news;
    private Integer parent;
    private String email;
    private String content;
}
