package com.ecnu.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author onion
 * @date 2019/12/10 -11:00 下午
 */
@Data
@Entity
@Table(name = "comment")
public class NewsComment implements Serializable {
    @Id
    @Column(name = "comment_id")
    private String commentId;
    @Column(name = "comment_news_id")
    private String commentNewsId;
    @Column(name = "parent_id")
    private String parentId;
    @Column
    private String email;
    @Column
    private String nickname;
    @Column
    private String content;
    @ManyToOne(targetEntity = News.class)
    @JoinColumn(name = "comment_news_id", referencedColumnName = "news_id",
            insertable = false, updatable = false)
    private News news;
}
