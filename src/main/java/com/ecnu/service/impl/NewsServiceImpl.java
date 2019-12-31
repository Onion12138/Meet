package com.ecnu.service.impl;

import com.ecnu.dao.CommentMapper;
import com.ecnu.dao.NewsDao;
import com.ecnu.dao.NewsMapper;
import com.ecnu.domain.NewsComment;
import com.ecnu.domain.News;
import com.ecnu.dto.CommentRequest;
import com.ecnu.dto.NewsRequest;
import com.ecnu.service.NewsService;
import com.ecnu.utils.JwtUtil;
import com.ecnu.utils.KeyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private NewsDao newsDao;
    @Autowired
    private CommentMapper commentMapper;
    @Override
    public PageInfo<News> findAllNews(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        PageHelper.orderBy("update_time");
        List<News> news = newsMapper.selectAll();
        return new PageInfo<>(news);
    }

    @Override
    public PageInfo<News> findTodayNews(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        PageHelper.orderBy("publish_time");
        Example example = new Example(News.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andBetween("publishTime", LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0))
                , LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59)));
        return new PageInfo<>(newsMapper.selectByExample(example));
    }

    @Override
    public News findOneNews(String newsId) {
        return newsDao.findById(newsId).get();
    }
    @Override
    public void addComment(CommentRequest request, String token) {
        Claims claims = JwtUtil.parseJwt(token);
        String userId = claims.getId();
        NewsComment comment = new NewsComment();
        comment.setCommentId(KeyUtil.genUniqueKey());
        comment.setEmail(userId);
        comment.setCommentNewsId(request.getNewsId());
        comment.setParentId(request.getParentId());
        comment.setContent(request.getContent());
        comment.setNickname((String)claims.get("nickname"));
        commentMapper.insert(comment);
    }

    @Override
    public void deleteComment(String commentId) {
        commentMapper.deleteByPrimaryKey(commentId);
    }
    @Override
    public void addNews(NewsRequest newsRequest, String token) {
        Claims claims = JwtUtil.parseJwt(token);
        String nickname = (String) claims.get("nickname");
        News news = new News();
        news.setTitle(newsRequest.getTitle());
        news.setNewsId(KeyUtil.genUniqueKey());
        news.setNickname(nickname);
        news.setEmail(claims.getId());
        news.setPublishTime(LocalDateTime.now());
        news.setUpdateTime(LocalDateTime.now());
        news.setContent(newsRequest.getContent());
        newsMapper.insert(news);
    }

    @Override
    public void updateNews(NewsRequest newsRequest) {
        News news = new News();
        news.setTitle(newsRequest.getTitle());
        news.setNewsId(newsRequest.getNewsId());
        news.setUpdateTime(LocalDateTime.now());
        news.setContent(newsRequest.getContent());
        newsMapper.updateByPrimaryKeySelective(news);
    }
    @Override
    public void deleteNews(String newsId) {
        newsDao.deleteById(newsId);
    }


}
