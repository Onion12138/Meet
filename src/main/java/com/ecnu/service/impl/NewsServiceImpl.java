package com.ecnu.service.impl;

import com.ecnu.dao.CommentMapper;
import com.ecnu.dao.NewsMapper;
import com.ecnu.domain.Comment;
import com.ecnu.domain.News;
import com.ecnu.dto.CommentRequest;
import com.ecnu.dto.NewsRequest;
import com.ecnu.service.NewsService;
import com.ecnu.utils.JwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author onion
 * @date 2019/12/11 -10:26 下午
 */
@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsMapper newsMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Override
    public PageInfo<News> findAllNews(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<News> news = newsMapper.selectAll();
        return new PageInfo<>(news);
    }

    @Override
    public PageInfo<News> findTodayNews(Integer page, Integer size) {
        //如何比较时间？
//        Example example = new Example();
//        Example.Criteria criteria = example.createCriteria();
//        criteria.
        return null;
    }

    @Override
    public void addComment(CommentRequest request) {
        String token = request.getToken();
        Claims claims = JwtUtil.parseJwt(token);
        String userId = claims.getId();
        Comment comment = new Comment();
        comment.setPublisher(userId);
        comment.setNewsId(request.getNewsId());
        comment.setParentId(request.getParentId());
        comment.setContent(request.getContent());
        commentMapper.insert(comment);
//        newsMapper.insert(comment);
    }

    @Override
    public void deleteComment(String commentId) {
        commentMapper.deleteByPrimaryKey(commentId);
    }
    //这个接口要改
    @Override
    public void addNews(NewsRequest newsRequest) {
        Claims claims = JwtUtil.parseJwt(newsRequest.getToken());
        String nickname = (String) claims.get("nickname");
        News news = new News();
        news.setNickname(nickname);
        news.setPublisher(claims.getId());
        news.setPublishTime(LocalDateTime.now());
        news.setUpdateTime(LocalDateTime.now());
        news.setContent(newsRequest.getContent());
        newsMapper.insert(news);
    }

    @Override
    public void updateNews(NewsRequest newsRequest) {
        News news = new News();
        news.setId(newsRequest.getNewsId());
        news.setUpdateTime(LocalDateTime.now());
        news.setContent(newsRequest.getContent());
        newsMapper.updateByPrimaryKeySelective(news);
    }

    @Override
    public void deleteNews(String newsId) {
        newsMapper.deleteByPrimaryKey(newsId);
    }
}
