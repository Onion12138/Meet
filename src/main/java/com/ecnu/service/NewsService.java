package com.ecnu.service;

import com.ecnu.domain.News;
import com.ecnu.dto.CommentRequest;
import com.ecnu.dto.NewsRequest;
import com.github.pagehelper.PageInfo;

/**
 * @author onion
 * @date 2019/12/11 -6:24 下午
 */
public interface NewsService {
    PageInfo<News> findAllNews(Integer page, Integer size);
    PageInfo<News> findTodayNews(Integer page, Integer size);

    void addComment(CommentRequest request);

    void deleteComment(String commentId);

    void addNews(NewsRequest newsRequest);

    void updateNews(NewsRequest newsRequest);

    void deleteNews(String newsId);
}
