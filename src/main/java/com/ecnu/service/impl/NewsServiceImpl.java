package com.ecnu.service.impl;

import com.ecnu.dao.CommentMapper;
import com.ecnu.dao.NewsDao;
import com.ecnu.dao.NewsMapper;
import com.ecnu.domain.News;
import com.ecnu.domain.NewsComment;
import com.ecnu.exception.MyException;
import com.ecnu.request.CommentRequest;
import com.ecnu.request.NewsRequest;
import com.ecnu.service.NewsService;
import com.ecnu.utils.JwtUtil;
import com.ecnu.utils.KeyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

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
        PageHelper.orderBy("update_time desc");
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
    public Map<String, Object> findOneNews(String newsId) {
        Optional<News> optional = newsDao.findById(newsId);
        if (optional.isPresent()) {
            News news = optional.get();
            Set<NewsComment> commentSet = news.getCommentSet();
//            List<NewsComment> orderList = new ArrayList<>();
            List<NewsComment> orderList = new LinkedList<>();
            commentSet.stream().filter(e->StringUtils.isEmpty(e.getParentId())).sorted(Comparator.comparing(NewsComment::getPublishTime)).forEach(orderList::add);
            commentSet.stream().filter(e->!StringUtils.isEmpty(e.getParentId())).sorted(Comparator.comparing(NewsComment::getPublishTime)).forEach(e->{
                for (int i = 0; i < orderList.size(); i++) {
                    if (e.getParentId().equals(orderList.get(i).getCommentId())) {
                        orderList.add(i + 1, e);
                        break;
                    }
                }
            });
            Map<String, Object> map = new HashMap<>();
            map.put("news", news);
            map.put("comment", orderList);
            return map;
        }else {
            throw new MyException("当前新闻不存在", -1);
        }
    }
    @Override
    public NewsComment addComment(CommentRequest request, String token) {
        Claims claims = JwtUtil.parseJwt(token);
        String userId = claims.getId();
        NewsComment comment = new NewsComment();
        comment.setCommentId(KeyUtil.genUniqueKey());
        comment.setEmail(userId);
        comment.setCommentNewsId(request.getNewsId());
        comment.setProfile(request.getProfile());
        comment.setPublishTime(LocalDateTime.now());
        comment.setParentName(request.getParentName());
        comment.setParentId(request.getParentId());
        comment.setContent(request.getContent());
        comment.setNickname((String)claims.get("nickname"));
        commentMapper.insert(comment);
        return comment;
    }

    @Override
    public void deleteComment(String commentId) {
        commentMapper.deleteByPrimaryKey(commentId);
    }
    @Override
    public News addNews(NewsRequest newsRequest, String token) {
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
        return news;
    }

    @Override
    public News updateNews(NewsRequest newsRequest) {
        News news = new News();
        news.setTitle(newsRequest.getTitle());
        news.setNewsId(newsRequest.getNewsId());
        news.setUpdateTime(LocalDateTime.now());
        news.setContent(newsRequest.getContent());
        newsMapper.updateByPrimaryKeySelective(news);
        return newsMapper.selectOne(news);
    }
    @Override
    public void deleteNews(String newsId) {
        newsDao.deleteById(newsId);
    }


}
