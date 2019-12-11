package com.ecnu.service.impl;

import com.ecnu.domain.News;
import com.ecnu.dto.CommentRequest;
import com.ecnu.dto.NewsRequest;
import com.ecnu.service.NewsService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

/**
 * @author onion
 * @date 2019/12/11 -10:26 下午
 */
@Service
public class NewsServiceImpl implements NewsService {
    @Override
    public PageInfo<News> findAllNews(Integer page, Integer size) {
        return null;
    }

    @Override
    public PageInfo<News> findTodayNews(Integer page, Integer size) {
        return null;
    }

    @Override
    public void addComment(CommentRequest request) {

    }

    @Override
    public void deleteComment(String commentId) {

    }

    @Override
    public void addNews(NewsRequest newsRequest) {

    }

    @Override
    public void updateNews(NewsRequest newsRequest) {

    }

    @Override
    public void deleteNews(String newsId) {

    }
}
