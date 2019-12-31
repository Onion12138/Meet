package com.ecnu.controller;

import com.ecnu.dao.UserDao;
import com.ecnu.domain.Gym;
import com.ecnu.domain.User;
import com.ecnu.request.GymFilterRequest;
import com.ecnu.utils.JwtUtil;
import com.ecnu.vo.ResultVO;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
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
public class IntegrationTestForGymController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TestRestTemplate restTemplate;

    private final static String REQUEST_MAPPING = "/gym";

    private final static String SUCCESS_MSG = "成功";

    private final static int OK = 200;

    private String tokenForUser;

    private String tokenForAdmin;

    private User testUser;

    private User admin;

    @BeforeEach
    public void choseTestUserAndAdmin() {
        testUser = userDao.findById("leodpen@gmail.com").get();
        log.info(testUser.getAdmin().toString());
        admin = userDao.findById("969023014@qq.com").get();
        tokenForUser = JwtUtil.createJwt(testUser);
        log.info(tokenForUser);
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
    @DisplayName("测试查看所有的场馆，需要登陆")
    @Transactional
    public void testFindAllGyms() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForUser);

        Map<String,Integer> map = new HashMap<>();
        map.put("page",1);
        map.put("size",5);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/allGyms?page={page}&size={size}",
                HttpMethod.GET,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assert result != null;
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
//                    assertEquals(1,((PageInfo) result.getData()).getPageNum());
//                    assertEquals(5,((PageInfo) result.getData()).getPageSize());
                    assertEquals(1,((LinkedHashMap) result.getData()).get("pageNum"));
                    assertEquals(5,((LinkedHashMap) result.getData()).get("pageSize"));
                }
        );

    }

    @Test
    @DisplayName("测试通过模糊查找查看所有的场馆，需要登陆")
    @Transactional
    public void testKeywords() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForUser);

        Map<String,Object> map = new HashMap<>();
        map.put("page",1);
        map.put("size",5);
        map.put("keyword","乒乓球");
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/keyword?page={page}&size={size}&keyword={keyword}",
                HttpMethod.GET,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assert result != null;
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                    assertEquals(1,((LinkedHashMap) result.getData()).get("pageNum"));
                    assertEquals(5,((LinkedHashMap) result.getData()).get("pageSize"));
                }
        );
    }

    @Test
    @DisplayName("测试通过过滤器高级查找来查看所有的场馆，需要登陆")
    @Transactional
    public void testFilter() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForUser);

        Map<String,Integer> map = new HashMap<>();
        map.put("page",1);
        map.put("size",5);
        GymFilterRequest request = GymFilterRequest.builder()
                .address("大学生活动中心一楼")
                .highToLow(true)
                .openOnly(true)
                .type("乒乓球")
                .build();
        HttpEntity<GymFilterRequest> entity = new HttpEntity<>(request,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/filter?page={page}&size={size}",
                HttpMethod.POST,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assert result != null;
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                    assertEquals(1,((LinkedHashMap) result.getData()).get("pageNum"));
                    assertEquals(5,((LinkedHashMap) result.getData()).get("pageSize"));
                }
        );
    }





}
