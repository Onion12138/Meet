package com.ecnu.servicetest;

import com.ecnu.daotest.CommentMapper;
import com.ecnu.daotest.NewsDao;
import com.ecnu.daotest.NewsMapper;
import com.ecnu.domain.News;
import com.ecnu.domain.NewsComment;
import com.ecnu.domain.User;
import com.ecnu.request.CommentRequest;
import com.ecnu.request.NewsRequest;
import com.ecnu.servicetest.impl.NewsServiceImpl;
import com.ecnu.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author onion
 * @date 2019/12/31 -10:27 上午
 */
@SpringBootTest
public class NewsServiceTest {
    @InjectMocks
    private NewsServiceImpl newsService;
    @Mock
    private NewsMapper newsMapper;
    @Mock
    private NewsDao newsDao;
    @Mock
    private CommentMapper commentMapper;
    private String token;
    private String newsId;
    private User user;
    private NewsRequest newsRequest;
    @BeforeEach
    public void init() {
        user = new User();
        user.setEmail("969023014@qq.com");
        user.setAdmin(true);
        user.setNickname("Onion");
        token = JwtUtil.createJwt(user);
        newsId = "1576853552102104862";
        newsRequest = new NewsRequest();
        newsRequest.setTitle("冬季恒温游泳馆面向本科生开放了");
        newsRequest.setNewsId(newsId);
        newsRequest.setContent("欢迎你的到来");
    }
    @Test
    @DisplayName("测试查询全部新闻")
    public void testFindAllNews() {
        newsService.findAllNews(1, 5);
        verify(newsMapper).selectAll();
    }
    @Test
    @DisplayName("测试查询今天的新闻")
    public void testFindTodayNews() {
        newsService.findTodayNews(1, 5);
        ArgumentCaptor<Example> captor = ArgumentCaptor.forClass(Example.class);
        verify(newsMapper).selectByExample(captor.capture());
        assertTrue(captor.getValue().getOredCriteria().size() > 0);
    }
    @Test
    @DisplayName("测试查询某个新闻")
    public void testFindOneNews() {
        when(newsDao.findById(anyString())).thenReturn(Optional.of(new News()));
        newsService.findOneNews(newsId);
        verify(newsDao).findById(anyString());
    }
    @Test
    @DisplayName("测试添加评论")
    public void testAddComment() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setNewsId(newsId);
        commentRequest.setContent("那是真的牛皮");
        newsService.addComment(commentRequest, token);
        ArgumentCaptor<NewsComment> captor = ArgumentCaptor.forClass(NewsComment.class);
        verify(commentMapper).insert(captor.capture());
        assertAll(() -> assertFalse(StringUtils.isEmpty(captor.getValue().getCommentId())),
                () -> assertEquals(user.getEmail(), captor.getValue().getEmail()),
                () -> assertEquals(commentRequest.getNewsId(), captor.getValue().getCommentNewsId()),
                () -> assertEquals(commentRequest.getParentId(), captor.getValue().getParentId()),
                () -> assertEquals(commentRequest.getContent(), captor.getValue().getContent()),
                () -> assertEquals(user.getNickname(), captor.getValue().getNickname()));
    }
    @Test
    @DisplayName("测试删除评论")
    public void testDeleteComment() {
        newsService.deleteComment(newsId);
        verify(commentMapper).deleteByPrimaryKey(anyString());
    }
    @Test
    @DisplayName("测试管理员发布新闻")
    public void testAddNews() {
        newsService.addNews(newsRequest, token);
        ArgumentCaptor<News> captor = ArgumentCaptor.forClass(News.class);
        verify(newsMapper).insert(captor.capture());
        assertAll(() -> assertEquals(newsRequest.getTitle(), captor.getValue().getTitle()),
                () -> assertFalse(StringUtils.isEmpty(captor.getValue().getNewsId())),
                () -> assertEquals(user.getNickname(), captor.getValue().getNickname()),
                () -> assertEquals(user.getEmail(), captor.getValue().getEmail()),
                () -> assertNotNull(captor.getValue().getPublishTime()),
                () -> assertNotNull(captor.getValue().getUpdateTime()),
                () -> assertEquals(newsRequest.getContent(), captor.getValue().getContent()));
    }
    @Test
    @DisplayName("测试管理员更新新闻")
    public void testUpdateNews() {
        LocalDateTime time = LocalDateTime.now();
        newsService.updateNews(newsRequest);
        ArgumentCaptor<News> captor = ArgumentCaptor.forClass(News.class);
        verify(newsMapper).updateByPrimaryKeySelective(captor.capture());
        assertAll(() -> assertEquals(newsRequest.getTitle(), captor.getValue().getTitle()),
                () -> assertEquals(time.getDayOfYear(), captor.getValue().getUpdateTime().getDayOfYear()),
                () -> assertEquals(newsRequest.getContent(), captor.getValue().getContent()),
                () -> assertEquals(newsRequest.getNewsId(), captor.getValue().getNewsId()));
    }
    @Test
    @DisplayName("测试管理员删除新闻")
    public void testDeleteNews() {
        newsService.deleteNews(newsId);
        verify(newsDao).deleteById(anyString());
    }

}
