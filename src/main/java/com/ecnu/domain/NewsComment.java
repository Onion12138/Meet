package com.ecnu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author onion
 * @date 2019/12/10 -11:00 下午
 */
@Getter
@Setter
@ToString
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
    @Column
    private String profile;
    @Column(name = "parent_name")
    private String parentName;
    @Column(name = "publish_time")
    private LocalDateTime publishTime;
    @ManyToOne(targetEntity = News.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_news_id", referencedColumnName = "news_id",
            insertable = false, updatable = false)
    @JsonIgnoreProperties("commentSet")
    private News news;
}
