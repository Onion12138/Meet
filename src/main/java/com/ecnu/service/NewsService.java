package com.ecnu.service;

import com.ecnu.domain.News;
import com.ecnu.domain.NewsComment;
import com.ecnu.request.CommentRequest;
import com.ecnu.request.NewsRequest;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @author onion
 * @date 2019/12/11 -6:24 下午
 */
public interface NewsService {
    PageInfo<News> findAllNews(Integer page, Integer size);

    PageInfo<News> findTodayNews(Integer page, Integer size);

    NewsComment addComment(CommentRequest request, String token);

    void deleteComment(String commentId);

    News addNews(NewsRequest newsRequest, String token);

    News updateNews(NewsRequest newsRequest);

    void deleteNews(String newsId);

    Map<String, Object> findOneNews(String newsId);
}
