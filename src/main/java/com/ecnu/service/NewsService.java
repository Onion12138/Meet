package com.ecnu.service;

import com.ecnu.domain.News;
import com.ecnu.request.CommentRequest;
import com.ecnu.request.NewsRequest;
import com.github.pagehelper.PageInfo;

/**
 * @author onion
 * @date 2019/12/11 -6:24 下午
 */
public interface NewsService {
    PageInfo<News> findAllNews(Integer page, Integer size);

    PageInfo<News> findTodayNews(Integer page, Integer size);

    void addComment(CommentRequest request, String token);

    void deleteComment(String commentId);

    void addNews(NewsRequest newsRequest, String token);

    void updateNews(NewsRequest newsRequest);

    void deleteNews(String newsId);

    News findOneNews(String newsId);
}
