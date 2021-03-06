package com.ecnu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @date 2019/12/10 -10:55 下午
 */
@Getter
@Setter
@Entity
@Table(name = "news")
public class News implements Serializable {
    @Override
    public String toString() {
        return "News{" +
                "newsId='" + newsId + '\'' +
                ", title='" + title + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", publishTime=" + publishTime +
                ", updateTime=" + updateTime +
                ", content='" + content + '\'' +
                '}';
    }

    @Id
    @Column(name = "news_id")
    private String newsId;
    @Column
    private String title;
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
//    @OneToMany(targetEntity = NewsComment.class, fetch = FetchType.LAZY)
//    @JoinColumn(name = "comment_news_id", referencedColumnName = "news_id")
//    private Set<NewsComment> commentSet;
    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("news")
    @JsonIgnore
//    @JoinColumn(name = "comment_news_id", referencedColumnName = "news_id")
    private Set<NewsComment> commentSet;
}
