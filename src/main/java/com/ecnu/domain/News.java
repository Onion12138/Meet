package com.ecnu.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author onion
 * @date 2019/12/10 -10:55 下午
 */
@Data
@Entity
@Table(name = "news")
public class News implements Serializable {
    @Id
    @Column(name = "news_id")
    private String newsId;
    @Column
    private String email;
    @Column
    private String nickname;
    @Column(name = "publish_time")
    private LocalDateTime publishTime;
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    @Column
    private String content;
    @OneToMany(mappedBy = "news")
    private Set<NewsComment> commentSet;
}
