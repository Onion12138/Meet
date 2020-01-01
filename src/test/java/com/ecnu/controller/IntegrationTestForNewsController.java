package com.ecnu.controller;

import com.ecnu.dao.UserDao;
import com.ecnu.domain.User;
import com.ecnu.request.CommentRequest;
import com.ecnu.request.NewsRequest;
import com.ecnu.utils.JwtUtil;
import com.ecnu.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Slf4j
public class IntegrationTestForNewsController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TestRestTemplate restTemplate;

    private final static String REQUEST_MAPPING = "/news";

    private final static String SUCCESS_MSG = "成功";

    private final static int OK = 200;

    private final static String toTestNewsId = "1577683872559839329";

    private String tokenForUser;

    private String tokenForAdmin;

    private User testUser;

    private User admin;
    @BeforeEach
    public void choseTestUserAndAdmin() {
        testUser = userDao.findById("leodpen@gmail.com").get();
        admin = userDao.findById("969023014@qq.com").get();
        tokenForUser = JwtUtil.createJwt(testUser);
        tokenForAdmin = JwtUtil.createJwt(admin);
        redisTemplate.opsForValue().set(tokenForAdmin,admin.getEmail(),144, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(tokenForUser,testUser.getEmail(),144, TimeUnit.SECONDS);
    }

    @AfterEach
    public void deleteTokens() {
        redisTemplate.delete(tokenForAdmin);
        redisTemplate.delete(tokenForUser);
    }

    @Test
    @DisplayName("测试查看所有的新闻，需要登陆")
    @Transactional
    public void testFindAllNews() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForUser);
        Map<String,Integer> map = new HashMap<>();
        map.put("page",1);
        map.put("size",5);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/all?page={page}&size={size}",
                HttpMethod.GET,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                    assertEquals(1,((LinkedHashMap) result.getData()).get("pageNum"));
                    assertEquals(5,((LinkedHashMap) result.getData()).get("pageSize"));
                }
        );
    }
    @Test
    @DisplayName("测试查看某一个新闻，但不存在相关新闻，需要登陆")
    @Transactional
    public void testBadFindCertainNewsWithComments() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForUser);
        Map<String,String> map = new HashMap<>();
        map.put("newsId","bad_news_id");
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/one?newsId={newsId}",
                HttpMethod.GET,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(-1,result.getCode());
                    assertEquals("当前新闻不存在",result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("测试成功查看某一个新闻，返回新闻与相关评论，需要登陆")
    @Transactional
    public void testFindCertainNewsWithComments() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForUser);
        Map<String,String> map = new HashMap<>();
        map.put("newsId",toTestNewsId);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/one?newsId={newsId}",
                HttpMethod.GET,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                    assertTrue(((LinkedHashMap)result.getData()).containsKey("news"));
                    assertTrue(((LinkedHashMap)result.getData()).containsKey("comment"));
                }
        );
    }

    @Test
    @DisplayName("测试成功添加评论")
    @Transactional
    public void testAddComment(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForUser);
        CommentRequest request = CommentRequest.builder()
                .newsId(toTestNewsId)
                .parentId("")
                .profile(testUser.getProfile())
                .content("那是真的牛皮")
                .parentName("")
                .build();
        HttpEntity<CommentRequest> entity = new HttpEntity<>(request,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.postForEntity(
                REQUEST_MAPPING + "/addComment",
                entity,
                ResultVO.class
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                    assertEquals(testUser.getEmail(),((LinkedHashMap)result.getData()).get("email"));
                    assertEquals(request.getContent(),((LinkedHashMap)result.getData()).get("content"));
                }
        );
    }

    @Test
    @DisplayName("测试成功删除评论，类似微信，只能删除和添加")
    @Transactional
    public void testDeleteComment(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForUser);
        Map<String,String> map = new HashMap<>();
        map.put("commentId","1577859419371241813");
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.postForEntity(
                REQUEST_MAPPING + "/deleteComment?commentId={commentId}",
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("测试添加news，需要管理员的登陆")
    @Transactional
    public void testAddNews() {
        NewsRequest request = NewsRequest.builder()
                .content("是的这只是一个test")
                .title("震惊，德玛西亚重出江湖？")
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        HttpEntity<NewsRequest> entity = new HttpEntity<>(request,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.postForEntity(REQUEST_MAPPING + "/addNews",
                entity,
                ResultVO.class);
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                    assertEquals( request.getTitle(),((LinkedHashMap)result.getData()).get("title"));
                    assertEquals( admin.getEmail(),((LinkedHashMap)result.getData()).get("email"));
                    assertEquals( request.getContent(),((LinkedHashMap)result.getData()).get("content"));
                }
        );
    }

    @Test
    @DisplayName("测试更新News，需要管理员的登陆")
    @Transactional
    public void testUpdateNews() {
        NewsRequest request = NewsRequest.builder()
                .newsId("1577860458170502678")
                .content("是的这只是一个test的update")
                .title("震惊，德玛西亚重出江湖？")
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        HttpEntity<NewsRequest> entity = new HttpEntity<>(request,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.postForEntity(REQUEST_MAPPING + "/updateNews",
                entity,
                ResultVO.class);
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                    assertEquals( request.getNewsId(),((LinkedHashMap)result.getData()).get("newsId"));
                    assertEquals( request.getContent(),((LinkedHashMap)result.getData()).get("content"));
                    assertEquals( request.getTitle(),((LinkedHashMap)result.getData()).get("title"));
                    assertEquals( admin.getEmail(),((LinkedHashMap)result.getData()).get("email"));
                }
        );
    }


    @Test
    @DisplayName("测试删除news，需要管理员的登陆")
    @Transactional
    public void testDeleteNews() {
        Map<String,String> map = new HashMap<>();
        map.put("newsId","1577860750751112024");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.postForEntity(REQUEST_MAPPING + "/deleteNews?newsId={newsId}",
                entity,
                ResultVO.class,
                map);
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }




}
