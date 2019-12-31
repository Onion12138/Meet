package com.ecnu.controller;

import com.alibaba.fastjson.JSON;
import com.ecnu.domain.User;
import com.ecnu.request.CommentRequest;
import com.ecnu.request.NewsRequest;
import com.ecnu.service.NewsService;
import com.ecnu.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author onion
 * @date 2019/12/26 -12:59 下午
 * 删除接口、异常情况暂未测试
 */
@SpringBootTest
@AutoConfigureMockMvc
public class NewsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NewsService newsService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private String token;
    private String adminToken;
    @BeforeEach
    public void init(){
        User user = new User();
        User admin = new User();
        user.setEmail("969023014@qq.com");
        user.setNickname("onion");
        user.setAdmin(false);
        token = JwtUtil.createJwt(user);
        admin.setEmail(user.getEmail());
        admin.setNickname(user.getNickname());
        admin.setAdmin(true);
        adminToken = JwtUtil.createJwt(admin);
        redisTemplate.opsForValue().set(token, user.getEmail());
        redisTemplate.opsForValue().set(adminToken, admin.getEmail());
    }
    @Test
    @DisplayName("用户登陆后查看所有的新闻")
    public void testFindAllNews() throws Exception {
        ResultActions perform = mockMvc.perform(get("/news/all").header("user_token",token));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(newsService).findAllNews(anyInt(), anyInt());
    }

    @Test
    @DisplayName("用户登陆后查看今天的新闻")
    public void testFindTodayNews() throws Exception {
        ResultActions perform = mockMvc.perform(get("/news/today").header("user_token",token));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(newsService).findTodayNews(anyInt(), anyInt());
    }

    @Test
    @DisplayName("用户查看某个新闻及其评论")
    public void testFindOneNews() throws Exception {
        String newsId = "1577023172697340825";
        ResultActions perform = mockMvc.perform(get("/news/one").param("newsId", newsId).header("user_token",token));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(newsService).findOneNews(anyString());
    }

    @Test
    @DisplayName("用户对某个新闻进行评论")
    public void testAddComment() throws Exception {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setNewsId("1577023172697340825");
        commentRequest.setContent("评论");
        String content = JSON.toJSONString(commentRequest);
        ResultActions perform = mockMvc.perform(post("/news/addComment").contentType(MediaType.APPLICATION_JSON)
                .content(content).header("user_token",token));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(newsService).addComment(any(), anyString());
    }

//    @Test
//    @DisplayName("用户发布空评论会抛出异常")
//    public void testAddEmptyComment() throws Exception {
//        CommentRequest commentRequest = new CommentRequest();
//        commentRequest.setNewsId("1577023172697340825");
//        commentRequest.setContent("");
//        String content = JSON.toJSONString(commentRequest);
//        ResultActions perform = mockMvc.perform(post("/news/addComment").contentType(MediaType.APPLICATION_JSON)
//                .content(content).header("user_token",token));
//        perform.andExpect(status().isOk());
//        perform.andExpect(jsonPath("$.code").value(-1));
//    }

    @Test
    @DisplayName("用户删除某个评论")
    public void testDeleteComment() throws Exception {
        ResultActions perform = mockMvc.perform(post("/news/deleteComment").param("commentId", "1576853552102104862").header("user_token",token));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(newsService).deleteComment(anyString());
    }

    @Test
    @DisplayName("管理员添加新闻")
    public void testAddNews() throws Exception {
        NewsRequest newsRequest = new NewsRequest();
        newsRequest.setTitle("news title");
        newsRequest.setContent("news content");
        String content = JSON.toJSONString(newsRequest);
        ResultActions perform = mockMvc.perform(post("/news/addNews").contentType(MediaType.APPLICATION_JSON).content(content).header("user_token",adminToken));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(newsService).addNews(any(), anyString());
    }

    @Test
    @DisplayName("管理员更新新闻")
    public void testUpdateNews() throws Exception {
        NewsRequest newsRequest = new NewsRequest();
        newsRequest.setNewsId("1577023172697340825");
        newsRequest.setTitle("news title");
        newsRequest.setContent("news content");
        String content = JSON.toJSONString(newsRequest);
        ResultActions perform = mockMvc.perform(post("/news/updateNews").contentType(MediaType.APPLICATION_JSON).content(content).header("user_token",adminToken));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(newsService).updateNews(any());
    }

}
